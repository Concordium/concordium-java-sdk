package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The provided index is below the start index or above `startIndex + length incomingAmounts`
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInvalidIndexOnEncryptedTransfer extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INDEX_ON_ENCRYPTED_TRANSFER;
    }
}
