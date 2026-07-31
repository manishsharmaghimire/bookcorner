package com.bookcorner.payment.dto.esewa;

import com.fasterxml.jackson.annotation.JsonProperty;
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


public class EsewaSuccessResponse {

    @JsonProperty("transaction_code")
    private String transactionCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("transaction_uuid")
    private String transactionUuid;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("signed_field_names")
    private String signedFieldNames;

    @JsonProperty("signature")
    private String signature;
}