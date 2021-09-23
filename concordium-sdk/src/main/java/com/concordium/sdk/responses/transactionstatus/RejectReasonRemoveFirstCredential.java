package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonRemoveFirstCredential extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.REMOVE_FIRST_CREDENTIAL;
    }
}
