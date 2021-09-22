package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidTransferToPublicProof extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_TRANSFER_TO_PUBLIC_PROOF;
    }
}
