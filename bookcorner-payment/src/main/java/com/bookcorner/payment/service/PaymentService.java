package com.bookcorner.payment.service;

import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse markPaymentSuccess(String transactionId);
    PaymentResponse markPaymentFailed(String transactionId);
    PaymentResponse getPaymentByOrder(String orderNumber);
}
