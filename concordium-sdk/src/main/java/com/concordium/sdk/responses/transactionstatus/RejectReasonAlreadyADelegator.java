package com.concordium.sdk.responses.transactionstatus;

/**
 * Tried to add baker for an account that already has a delegator
 */
public class RejectReasonAlreadyADelegator extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ALREADY_A_DELEGATOR;
    }
}
