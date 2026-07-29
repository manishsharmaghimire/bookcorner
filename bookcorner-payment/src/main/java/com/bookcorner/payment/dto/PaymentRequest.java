package com.bookcorner.payment.dto;

import com.bookcorner.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {


    private String orderNumber;

    private PaymentMethod paymentMethod;
}
