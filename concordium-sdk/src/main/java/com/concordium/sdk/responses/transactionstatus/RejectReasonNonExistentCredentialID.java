package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;

/**
 * Encountered credential ID that does not exist
 */
@EqualsAndHashCode
public class RejectReasonNonExistentCredentialID extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CRED_IDS;
    }
}
