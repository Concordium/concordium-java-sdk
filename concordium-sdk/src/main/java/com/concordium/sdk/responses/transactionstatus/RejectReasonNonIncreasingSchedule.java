package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNonIncreasingSchedule extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_INCREASING_SCHEDULE;
    }
}
