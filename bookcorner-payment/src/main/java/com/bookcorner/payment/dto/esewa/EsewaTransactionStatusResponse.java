package com.bookcorner.payment.dto.esewa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsewaTransactionStatusResponse {

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("transaction_uuid")
    private String transactionUuid;

    @JsonProperty("total_amount")
    private String totalAmount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ref_id")
    private String refId;
}
