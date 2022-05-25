package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OpenStatus {
    @JsonProperty("openForAll")
    OPEN_FOR_ALL,
    @JsonProperty("closedForNew")
    CLOSED_FOR_NEW,
    @JsonProperty("closedForAll")
    CLOSED_FOR_ALL
}
