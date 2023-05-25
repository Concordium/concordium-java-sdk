package com.concordium.sdk.responses.transactionstatus;



public interface TransactionResultEvent {
// Used for tagging the underlying TransactionResultEvents

    TransactionResultEventType getType();
}
