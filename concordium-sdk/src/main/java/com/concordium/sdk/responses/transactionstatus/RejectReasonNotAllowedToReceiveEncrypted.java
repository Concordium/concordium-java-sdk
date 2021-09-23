package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonNotAllowedToReceiveEncrypted extends RejectReason {

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_RECEIVE_ENCRYPTED;
    }
}
