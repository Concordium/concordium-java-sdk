package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonNotAllowedToReceiveEncrypted extends RejectReasonContent{

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_RECEIVE_ENCRYPTED;
    }
}
