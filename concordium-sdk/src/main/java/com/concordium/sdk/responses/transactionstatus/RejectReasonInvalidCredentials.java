package com.concordium.sdk.responses.transactionstatus;

class RejectReasonInvalidCredentials extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIALS;
    }
}
