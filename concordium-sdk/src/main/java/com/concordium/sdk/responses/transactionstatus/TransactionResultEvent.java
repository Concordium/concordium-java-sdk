package com.concordium.sdk.responses.transactionstatus;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class TransactionResultEvent {
// Used for tagging the underlying TransactionResultEvents

    public abstract TransactionResultEventType getType();
}
