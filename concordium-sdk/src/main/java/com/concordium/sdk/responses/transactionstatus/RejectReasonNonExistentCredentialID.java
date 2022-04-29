package com.concordium.sdk.responses.transactionstatus;

/**
 * Encountered credential ID that does not exist
 */
public class RejectReasonNonExistentCredentialID extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CRED_IDS;
    }
}
