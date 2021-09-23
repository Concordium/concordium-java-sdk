package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonNotAllowedToHandleEncrypted extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_HANDLE_ENCRYPTED;
    }
}
