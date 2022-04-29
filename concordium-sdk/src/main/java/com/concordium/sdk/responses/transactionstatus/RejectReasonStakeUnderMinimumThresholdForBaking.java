package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The amount on the account was insufficient to cover the proposed stake
 */
@ToString
public class RejectReasonStakeUnderMinimumThresholdForBaking extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING;
    }
}
