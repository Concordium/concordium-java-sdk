package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class DataRegisteredResult extends TransactionResultEvent {
    private final String data;

    @JsonCreator
    DataRegisteredResult(@JsonProperty("data") String data) {
        this.data = data;
    }
}
