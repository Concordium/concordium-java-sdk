package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Baking reward commission is not in the valid range for a baker.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonBakingRewardCommissionNotInRange extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.BAKING_REWARD_COMMISSION_NOT_IN_RANGE;
    }
}
