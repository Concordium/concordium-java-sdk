package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Attempted to add an account key to a key index already in use
 */
@ToString
@EqualsAndHashCode
public class RejectReasonKeyIndexAlreadyInUse extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.KEY_INDEX_ALREADY_IN_USE;
    }
}
