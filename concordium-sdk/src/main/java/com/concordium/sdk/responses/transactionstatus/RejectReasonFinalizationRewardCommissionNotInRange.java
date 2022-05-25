package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * Finalization reward commission is not in the valid range for a baker.
 */
@ToString
public class RejectReasonFinalizationRewardCommissionNotInRange extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE;
    }
}
