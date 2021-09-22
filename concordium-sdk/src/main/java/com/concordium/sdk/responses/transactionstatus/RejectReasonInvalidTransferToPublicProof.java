package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidTransferToPublicProof extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_TRANSFER_TO_PUBLIC_PROOF;
    }
}
