package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonStakeUnderMinimumThresholdForBaking extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING;
    }
}
