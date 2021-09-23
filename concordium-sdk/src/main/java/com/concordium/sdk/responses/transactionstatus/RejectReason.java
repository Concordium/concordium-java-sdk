package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;
import lombok.ToString;

// Used for tagging the underlying RejectReasonContent
@Getter
@ToString
public abstract class RejectReason {
    public abstract RejectReasonType getType();
}
