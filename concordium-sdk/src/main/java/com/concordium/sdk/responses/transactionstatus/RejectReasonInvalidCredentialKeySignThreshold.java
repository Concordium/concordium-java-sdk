package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidCredentialKeySignThreshold extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD;
    }
}
