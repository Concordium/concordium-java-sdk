package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidCredentialKeySignThreshold extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD;
    }
}
