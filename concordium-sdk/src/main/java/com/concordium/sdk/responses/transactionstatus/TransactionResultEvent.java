package com.concordium.sdk.responses.transactionstatus;


public abstract class TransactionResultEvent {
// Used for tagging the underlying TransactionResultEvents

    public abstract TransactionResultEventType getType();
}
