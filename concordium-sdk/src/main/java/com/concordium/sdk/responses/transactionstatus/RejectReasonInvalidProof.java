package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidProof extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_PROOF;
    }
}
