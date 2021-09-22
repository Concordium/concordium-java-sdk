package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNotAllowedToHandleEncrypted extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_HANDLE_ENCRYPTED;
    }
}
