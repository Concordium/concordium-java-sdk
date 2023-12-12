package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class AccountCreatedResult implements TransactionResultEvent {
    private final String contents;
    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ACCOUNT_CREATED;
    }
}
