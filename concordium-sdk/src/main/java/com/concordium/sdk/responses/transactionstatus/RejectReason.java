package com.concordium.sdk.responses.transactionstatus;

import lombok.Data;

// Used for tagging the underlying RejectReasonContent
@Data
public abstract class RejectReason {
    public abstract RejectReasonType getType();
}
