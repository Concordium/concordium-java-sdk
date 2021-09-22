package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidProof extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_PROOF;
    }
}
