package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * We ran out of energy while processing this transaction.
 */
@ToString
public class RejectReasonOutOfEnergy extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.OUT_OF_ENERGY;
    }
}
