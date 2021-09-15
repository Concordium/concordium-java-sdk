package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class TransactionTypeInfo {
    private final TransactionType type;
    private final TransactionContents contents;

    @JsonCreator
    TransactionTypeInfo(@JsonProperty("type") TransactionType type,
                        @JsonProperty("contents") TransactionContents contents) {
        this.type = type;
        this.contents = contents;
    }
}
