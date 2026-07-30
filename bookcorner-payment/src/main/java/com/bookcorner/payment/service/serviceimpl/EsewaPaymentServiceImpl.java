package com.bookcorner.payment.service.serviceimpl;


import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.exception.OrderNotFoundException;
import com.bookcorner.order.repository.OrderRepository;
import com.bookcorner.payment.config.EsewaConfiguration;
import com.bookcorner.payment.dto.esewa.EsewaPaymentRequest;
import com.bookcorner.payment.entity.Payment;
import com.bookcorner.payment.enums.PaymentStatus;
import com.bookcorner.payment.exception.PaymentAlreadyExistsException;
import com.bookcorner.payment.exception.PaymentNotFoundException;
import com.bookcorner.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EsewaPaymentServiceImpl {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final EsewaConfiguration esewaProperties;


    public EsewaPaymentRequest createPaymentRequest(String orderNumber) {


        var order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with order number: " + orderNumber));


        Payment payment = paymentRepository
                .findByOrder(order)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found."
                        ));


        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Payment has already been completed."
            );}

            var amount = payment.getAmount().toString();
            var transactionUuid = order.getOrderNumber();
            String productCode = esewaProperties.getMerchantId();
            // 5. Generate signature
            String signedData =
                    "total_amount=" + amount +
                            ",transaction_uuid=" + transactionUuid +
                            ",product_code=" + productCode;

        var signature = generateSignature(signedData);
        // 6. Build request
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
                .signature(signature)
                .build();

    }



        private String generateSignature(String data) {

            try {

                Mac mac = Mac.getInstance("HmacSHA256");

                SecretKeySpec secretKeySpec = new SecretKeySpec(
                        esewaProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256"
                );

                mac.init(secretKeySpec);

                byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

                return Base64.getEncoder().encodeToString(hash);

            } catch (Exception e) {
                throw new RuntimeException("Failed to generate eSewa signature.", e);
            }
        }

}
