package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Proof for a secret to public transfer did not validate.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInvalidTransferToPublicProof extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_TRANSFER_TO_PUBLIC_PROOF;
    }
}
