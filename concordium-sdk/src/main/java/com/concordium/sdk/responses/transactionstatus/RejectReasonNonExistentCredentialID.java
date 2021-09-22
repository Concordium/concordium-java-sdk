package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNonExistentCredentialID extends RejectReasonContent{
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CRED_IDS;
    }
}
