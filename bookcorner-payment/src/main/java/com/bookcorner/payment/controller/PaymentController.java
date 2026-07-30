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

    @PostMapping("/success/{transactionId}")
    public ResponseEntity<PaymentResponse> markPaymentSuccess(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.markPaymentSuccess(transactionId));
    }

    @PostMapping("/failed/{transactionId}")
    public ResponseEntity<PaymentResponse> markPaymentFailed(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.markPaymentFailed(transactionId));
    }

    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderNumber));
    }

    @GetMapping("/esewa/{orderNumber}")
    public ResponseEntity<EsewaPaymentRequest> createEsewaPayment(
            @PathVariable String orderNumber
    ) {

        return ResponseEntity.ok(
                esewaService.createPaymentRequest(orderNumber)
        );
    }
}
