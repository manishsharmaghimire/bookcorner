package com.bookcorner.payment.dto;

import com.bookcorner.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
