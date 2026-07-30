package com.bookcorner.payment.dto.esewa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsewaPaymentRequest {

    private String amount;

    private String taxAmount;

    private String totalAmount;

    private String transactionUuid;

    private String productCode;

    private String productServiceCharge;

    private String productDeliveryCharge;

    private String successUrl;

    private String failureUrl;

    private String signedFieldNames;

    private String signature;
}