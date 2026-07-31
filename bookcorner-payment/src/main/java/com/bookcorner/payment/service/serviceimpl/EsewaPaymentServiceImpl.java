package com.bookcorner.payment.service.serviceimpl;

import com.bookcorner.order.exception.OrderNotFoundException;
import com.bookcorner.order.repository.OrderRepository;
import com.bookcorner.payment.config.EsewaProperties;
import com.bookcorner.payment.dto.esewa.EsewaPaymentRequest;
import com.bookcorner.payment.dto.esewa.EsewaSuccessResponse;
import com.bookcorner.payment.dto.esewa.EsewaTransactionStatusResponse;
import com.bookcorner.payment.entity.Payment;
import com.bookcorner.payment.enums.PaymentStatus;
import com.bookcorner.payment.exception.InvalidPaymentSignatureException;
import com.bookcorner.payment.exception.PaymentNotFoundException;
import com.bookcorner.payment.exception.PaymentVerificationException;
import com.bookcorner.payment.repository.PaymentRepository;
import com.bookcorner.payment.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class EsewaPaymentServiceImpl {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final EsewaProperties esewaProperties;
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    private final RestTemplate restTemplate;

    public EsewaPaymentRequest createPaymentRequest(String orderNumber) {

        var order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderNumber));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found."));

        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment already completed.");
        }

        String amount = payment.getAmount().toPlainString();
        String transactionUuid = order.getOrderNumber() + "-" + System.currentTimeMillis();
        String productCode = esewaProperties.getMerchantId();

        String signedData = "total_amount=" + amount
                + ",transaction_uuid=" + transactionUuid
                + ",product_code=" + productCode;

        log.info("eSewa payment request created for order: {}", orderNumber);

        return EsewaPaymentRequest.builder()
                .amount(amount)
                .taxAmount("0")
                .totalAmount(amount)
                .transactionUuid(transactionUuid)
                .productCode(productCode)
                .productServiceCharge("0")
                .productDeliveryCharge("0")
                .successUrl(esewaProperties.getSuccessUrl())
                .failureUrl(esewaProperties.getFailureUrl())
                .signedFieldNames("total_amount,transaction_uuid,product_code")
                .signature(generateSignature(signedData))
                .build();
    }

    public void verifyPayment(String encodedData) {

        log.info("eSewa payment verification started.");

        // 1. Decode Base64
        String decodedJson;
        try {
            decodedJson = new String(Base64.getDecoder().decode(encodedData), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new PaymentVerificationException("Invalid callback data.", ex);
        }

        // 2. Parse callback
        EsewaSuccessResponse callback;
        try {
            callback = objectMapper.readValue(decodedJson, EsewaSuccessResponse.class);
        } catch (JsonProcessingException ex) {
            throw new PaymentVerificationException("Unable to parse callback response.", ex);
        }

        // 3. Verify callback signature before trusting any callback fields
        verifyCallbackSignature(callback);

        // 4. Validate callback product code
        if (!esewaProperties.getMerchantId().equals(callback.getProductCode())) {
            log.warn("eSewa callback merchant mismatch for uuid: {}", callback.getTransactionUuid());
            throw new PaymentVerificationException("Invalid merchant in callback.");
        }

        // 5. Validate callback status
        if (!"COMPLETE".equalsIgnoreCase(callback.getStatus())) {
            log.warn("eSewa callback status is not COMPLETE: {}", callback.getStatus());
            throw new PaymentVerificationException("Payment not completed per callback.");
        }

        // 6. Load payment by merchantTransactionId and validate amount
        Payment payment = paymentRepository
                .findByMerchantTransactionId(callback.getTransactionUuid())
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment not found for transaction: " + callback.getTransactionUuid()
                ));

        // 7. Validate amount matches stored payment
        BigDecimal callbackAmount;
        try {
            callbackAmount = new BigDecimal(callback.getTotalAmount().replace(",", ""));
        } catch (NullPointerException | NumberFormatException ex) {
            throw new PaymentVerificationException("Invalid amount in eSewa callback.", ex);
        }
        if (payment.getAmount().compareTo(callbackAmount) != 0) {
            log.warn("Amount mismatch for order: {} — expected: {}, got: {}",
                    callback.getTransactionUuid(), payment.getAmount(), callbackAmount);
            throw new PaymentVerificationException("Amount mismatch.");
        }

        // 8. Verify with eSewa Transaction Status API
        EsewaTransactionStatusResponse status = checkTransactionStatus(
                callback.getTransactionUuid(),
                callback.getTotalAmount()
        );

        // 9. Validate merchant from eSewa response
        if (!esewaProperties.getMerchantId().equals(status.getProductCode())) {
            throw new PaymentVerificationException("Merchant code mismatch from eSewa.");
        }

        // 10. Validate status from eSewa response
        if (!"COMPLETE".equalsIgnoreCase(status.getStatus())) {
            throw new PaymentVerificationException("Payment not completed per eSewa.");
        }

        // 11. Validate amount from eSewa response
        BigDecimal verifiedAmount;
        try {
            verifiedAmount = new BigDecimal(status.getTotalAmount().replace(",", ""));
        } catch (NullPointerException | NumberFormatException ex) {
            throw new PaymentVerificationException("Invalid amount in eSewa verification response.", ex);
        }
        if (payment.getAmount().compareTo(verifiedAmount) != 0) {
            log.warn("eSewa verified amount mismatch for order: {}", callback.getTransactionUuid());
            throw new PaymentVerificationException("Amount mismatch from eSewa verification.");
        }

        log.info("eSewa payment verified for order: {}, refId: {}",
                callback.getTransactionUuid(), status.getRefId());

        // 12. Replay protection — unique constraint on gateway_transaction_id enforces atomicity;
        //     catch the violation instead of relying on a pre-check that has a TOCTOU gap.
        try {
            paymentService.markPaymentSuccess(callback.getTransactionUuid(), status.getRefId());
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new PaymentVerificationException(
                    "Gateway transaction ID already used: " + status.getRefId(), ex
            );
        }
    }

    private void verifyCallbackSignature(EsewaSuccessResponse response) {

        if (response.getSignedFieldNames() == null || response.getSignature() == null) {
            throw new InvalidPaymentSignatureException("Missing signature fields in callback.");
        }

        Map<String, String> fieldValues = Map.of(
                "transaction_code", nullToEmpty(response.getTransactionCode()),
                "status", nullToEmpty(response.getStatus()),
                "total_amount", nullToEmpty(response.getTotalAmount()),
                "transaction_uuid", nullToEmpty(response.getTransactionUuid()),
                "product_code", nullToEmpty(response.getProductCode()),
                "signed_field_names", response.getSignedFieldNames()
        );

        String signedData = Arrays.stream(response.getSignedFieldNames().split(","))
                .map(field -> field + "=" + fieldValues.getOrDefault(field.trim(), ""))
                .reduce((a, b) -> a + "," + b)
                .orElseThrow(() -> new InvalidPaymentSignatureException("Empty signed_field_names."));

        byte[] computed = Base64.getDecoder().decode(generateSignature(signedData));
        byte[] provided = Base64.getDecoder().decode(response.getSignature());

        if (!MessageDigest.isEqual(computed, provided)) {
            throw new InvalidPaymentSignatureException("Invalid callback signature.");
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private EsewaTransactionStatusResponse checkTransactionStatus(String transactionUuid, String totalAmount) {

        String url = UriComponentsBuilder
                .fromHttpUrl(esewaProperties.getVerificationUrl())
                .queryParam("product_code", esewaProperties.getMerchantId())
                .queryParam("total_amount", totalAmount)
                .queryParam("transaction_uuid", transactionUuid)
                .toUriString();

        ResponseEntity<EsewaTransactionStatusResponse> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, EsewaTransactionStatusResponse.class);
        } catch (RestClientException ex) {
            log.error("Unable to connect to eSewa verification API: {}", ex.getMessage());
            throw new PaymentVerificationException("Unable to connect to eSewa.", ex);
        }

        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            log.error("eSewa verification API returned non-2xx: {}", responseEntity.getStatusCode());
            throw new PaymentVerificationException("eSewa verification API error.");
        }

        return responseEntity.getBody();
    }

    private String generateSignature(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    esewaProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new PaymentVerificationException("Failed to generate eSewa signature.", e);
        }
    }
}
