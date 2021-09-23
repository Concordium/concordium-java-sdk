package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonCredentialHolderDidNotSign extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.CREDENTIAL_HOLDER_DID_NOT_SIGN;
    }
}
