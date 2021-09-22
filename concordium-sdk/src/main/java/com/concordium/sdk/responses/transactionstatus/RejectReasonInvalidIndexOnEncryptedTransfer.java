package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidIndexOnEncryptedTransfer extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INDEX_ON_ENCRYPTED_TRANSFER;
    }
}
