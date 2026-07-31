package com.bookcorner.payment.mapper;

import com.bookcorner.payment.dto.PaymentResponse;
import com.bookcorner.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderNumber(payment.getOrder().getOrderNumber())
                .userId(payment.getOrder().getUser().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getPaymentStatus())
                .transactionId(payment.getMerchantTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
