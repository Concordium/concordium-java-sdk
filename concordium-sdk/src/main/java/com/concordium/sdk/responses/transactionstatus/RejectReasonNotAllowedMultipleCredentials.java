package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNotAllowedMultipleCredentials extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_MULTIPLE_CREDENTIALS;
    }
}
