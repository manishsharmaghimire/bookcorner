package com.bookcorner.payment.controller;

import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;
import com.bookcorner.payment.dto.esewa.EsewaPaymentRequest;
import com.bookcorner.payment.dto.esewa.EsewaSuccessResponse;
import com.bookcorner.payment.exception.PaymentVerificationException;
import com.bookcorner.payment.service.PaymentService;
import com.bookcorner.payment.service.serviceimpl.EsewaPaymentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final EsewaPaymentServiceImpl esewaService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }

    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderNumber));
    }

    @GetMapping("/esewa/{orderNumber}")
    public ResponseEntity<EsewaPaymentRequest> createEsewaPayment(@PathVariable String orderNumber) {
        return ResponseEntity.ok(esewaService.createPaymentRequest(orderNumber));
    }

    @GetMapping("/esewa/success")
    public ResponseEntity<String> esewaSuccess(@RequestParam("data") String encodedData) {
        esewaService.verifyPayment(encodedData);
        return ResponseEntity.ok("Payment verified successfully.");
    }

    @GetMapping("/esewa/failure")
    public ResponseEntity<String> esewaFailure(@RequestParam("data") String encodedData) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encodedData), StandardCharsets.UTF_8);
            EsewaSuccessResponse response = objectMapper.readValue(decoded, EsewaSuccessResponse.class);
            paymentService.markPaymentFailed(response.getTransactionUuid());
        } catch (Exception ex) {
            log.warn("eSewa failure callback could not be parsed: {}", ex.getMessage());
            throw new PaymentVerificationException("Invalid eSewa failure callback.", ex);
        }
        return ResponseEntity.ok("Payment failed.");
    }
}
