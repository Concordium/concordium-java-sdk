package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Attempt to remove the first credential.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonRemoveFirstCredential extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REMOVE_FIRST_CREDENTIAL;
    }
}
