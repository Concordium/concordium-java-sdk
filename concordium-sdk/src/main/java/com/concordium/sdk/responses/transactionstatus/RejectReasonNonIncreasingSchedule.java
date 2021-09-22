package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonNonIncreasingSchedule extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_INCREASING_SCHEDULE;
    }
}
