package com.concordium.sdk.responsetypes.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Outcome {
    @JsonProperty("success")
    SUCCESS,
    @JsonProperty("reject")
    REJECT
}
