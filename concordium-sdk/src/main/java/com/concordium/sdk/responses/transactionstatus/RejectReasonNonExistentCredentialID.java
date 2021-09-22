package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNonExistentCredentialID extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CRED_IDS;
    }
}
