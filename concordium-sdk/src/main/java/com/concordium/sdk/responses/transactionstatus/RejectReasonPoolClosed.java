package com.concordium.sdk.responses.transactionstatus;


import lombok.Getter;
import lombok.ToString;

/**
 * The pool is not open to delegators.
 */
@ToString
@Getter
public class RejectReasonPoolClosed extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.POOL_CLOSED;
    }
}
