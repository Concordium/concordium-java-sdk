package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public final class AccountCreatedResult extends TransactionResultEvent {
    private final String contents;

    @JsonCreator
    AccountCreatedResult(@JsonProperty("contents") String contents) {
        this.contents = contents;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ACCOUNT_CREATED;
    }
}
