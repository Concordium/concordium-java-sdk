package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidAccountThreshold extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_THRESHOLD;
    }
}
