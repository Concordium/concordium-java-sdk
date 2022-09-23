package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class TransactionResultEvent {
// Used for tagging the underlying TransactionResultEvents

    public abstract TransactionResultEventType getType();
}
