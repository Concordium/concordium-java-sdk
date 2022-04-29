package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The transfer with schedule is going to send 0 tokens
 */
@ToString
public class RejectReasonZeroScheduledAmount extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ZERO_SCHEDULED_AMOUNT;
    }
}
