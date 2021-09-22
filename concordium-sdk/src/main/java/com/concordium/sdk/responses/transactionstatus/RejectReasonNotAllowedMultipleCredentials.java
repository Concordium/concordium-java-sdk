package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonNotAllowedMultipleCredentials extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_MULTIPLE_CREDENTIALS;
    }
}
