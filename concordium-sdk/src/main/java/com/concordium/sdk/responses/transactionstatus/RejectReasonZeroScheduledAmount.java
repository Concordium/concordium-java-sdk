package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonZeroScheduledAmount extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ZERO_SCHEDULED_AMOUNT;
    }
}
