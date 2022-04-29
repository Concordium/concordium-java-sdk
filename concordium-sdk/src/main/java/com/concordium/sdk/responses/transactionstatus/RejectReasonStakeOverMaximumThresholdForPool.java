package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The amount would result in pool capital higher than the maximum threshold
 */
@ToString
public class RejectReasonStakeOverMaximumThresholdForPool extends RejectReason {


    @Override
    public RejectReasonType getType() {
        return RejectReasonType.STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL;
    }
}
