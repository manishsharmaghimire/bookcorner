package com.bookcorner.payment.service;

import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse markPaymentSuccess(String orderNumber, String gatewayTransactionId);
    PaymentResponse markPaymentFailed(String orderNumber);
    PaymentResponse getPaymentByOrder(String orderNumber);
}
