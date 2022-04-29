package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The transfer with schedule has a non strictly increasing schedule
 */
@ToString
public class RejectReasonNonIncreasingSchedule extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_INCREASING_SCHEDULE;
    }
}
