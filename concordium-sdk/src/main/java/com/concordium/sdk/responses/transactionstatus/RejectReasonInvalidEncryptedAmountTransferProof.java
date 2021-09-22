package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidEncryptedAmountTransferProof extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF;
    }
}
