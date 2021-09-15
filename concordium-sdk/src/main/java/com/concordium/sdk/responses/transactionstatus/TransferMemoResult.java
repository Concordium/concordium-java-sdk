package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class TransferMemoResult extends TransactionResultEvent {
    private final String memo;

    @JsonCreator
    TransferMemoResult(@JsonProperty("memo") String memo) {
        this.memo = memo;
    }
}
