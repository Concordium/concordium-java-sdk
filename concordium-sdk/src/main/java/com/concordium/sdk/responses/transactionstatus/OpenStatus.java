package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OpenStatus {
    OPEN_FOR_ALL,
    CLOSED_FOR_NEW,
    CLOSED_FOR_ALL;

    @JsonValue
    public int toValue(){
        return ordinal();
    }
}
