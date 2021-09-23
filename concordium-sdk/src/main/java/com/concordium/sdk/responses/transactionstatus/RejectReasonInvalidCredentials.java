package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
class RejectReasonInvalidCredentials extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIALS;
    }
}
