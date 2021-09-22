package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonZeroScheduledAmount extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ZERO_SCHEDULED_AMOUNT;
    }
}
