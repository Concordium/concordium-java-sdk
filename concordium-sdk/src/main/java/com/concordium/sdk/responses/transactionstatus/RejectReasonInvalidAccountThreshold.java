package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * When the account threshold is updated, it must not exceed the amount of existing keys
 */
@ToString
public class RejectReasonInvalidAccountThreshold extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_THRESHOLD;
    }
}
