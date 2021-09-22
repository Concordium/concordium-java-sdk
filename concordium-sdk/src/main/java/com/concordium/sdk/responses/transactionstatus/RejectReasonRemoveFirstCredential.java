package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonRemoveFirstCredential extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REMOVE_FIRST_CREDENTIAL;
    }
}
