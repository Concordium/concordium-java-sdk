package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonStakeUnderMinimumThresholdForBaking extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING;
    }
}
