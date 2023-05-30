package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The amount would result in pool with a too high fraction of delegated capital.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonPoolWouldBecomeOverDelegated extends RejectReason {

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.POOL_WOULD_BECOME_OVER_DELEGATED;
    }
}
