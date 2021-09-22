package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidEncryptedAmountTransferProof extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF;
    }
}
