package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The amount on the account was insufficient to cover the proposed stake.
 */
@ToString
public class RejectReasonInsufficientBalanceForBakerStake extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INSUFFICIENT_BALANCE_FOR_BAKER_STAKE;
    }
}
