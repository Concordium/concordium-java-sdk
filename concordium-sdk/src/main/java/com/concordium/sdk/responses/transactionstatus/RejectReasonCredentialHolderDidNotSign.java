package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonCredentialHolderDidNotSign extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.CREDENTIAL_HOLDER_DID_NOT_SIGN;
    }
}
