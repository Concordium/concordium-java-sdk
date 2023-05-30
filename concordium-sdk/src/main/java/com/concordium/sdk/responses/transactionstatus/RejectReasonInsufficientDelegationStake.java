package com.concordium.sdk.responses.transactionstatus;


import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The delegation stake when adding a baker was 0.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInsufficientDelegationStake extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INSUFFICIENT_DELEGATION_STAKE;
    }
}
