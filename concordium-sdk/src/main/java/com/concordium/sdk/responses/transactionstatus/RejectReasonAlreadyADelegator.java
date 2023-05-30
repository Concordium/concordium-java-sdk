package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;

/**
 * Tried to add baker for an account that already has a delegator
 */
@EqualsAndHashCode
public class RejectReasonAlreadyADelegator extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_DELEGATOR;
    }
}
