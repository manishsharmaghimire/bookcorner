package com.bookcorner.payment.controller;

import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;
import com.bookcorner.payment.dto.esewa.EsewaPaymentRequest;
import com.bookcorner.payment.service.PaymentService;
import com.bookcorner.payment.service.serviceimpl.EsewaPaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final EsewaPaymentServiceImpl esewaService;

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
        // decode to get orderNumber and mark failed
        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(encodedData));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.bookcorner.payment.dto.esewa.EsewaSuccessResponse response =
                    mapper.readValue(decoded, com.bookcorner.payment.dto.esewa.EsewaSuccessResponse.class);
            paymentService.markPaymentFailed(response.getTransactionUuid());
        } catch (Exception ignored) {}
        return ResponseEntity.ok("Payment failed.");
    }
}
