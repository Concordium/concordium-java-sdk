package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonInvalidIndexOnEncryptedTransfer extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INDEX_ON_ENCRYPTED_TRANSFER;
    }
}
