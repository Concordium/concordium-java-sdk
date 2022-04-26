package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    @JsonProperty("received")
    RECEIVED,
    @JsonProperty("finalized")
    FINALIZED,
    @JsonProperty("committed")
    COMMITTED,
    @JsonProperty("absent")
    ABSENT
}
