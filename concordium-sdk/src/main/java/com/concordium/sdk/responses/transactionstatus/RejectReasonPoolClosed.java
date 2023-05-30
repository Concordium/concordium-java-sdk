package com.concordium.sdk.responses.transactionstatus;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The pool is not open to delegators.
 */
@ToString
@Getter
@EqualsAndHashCode
public class RejectReasonPoolClosed extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.POOL_CLOSED;
    }
}
