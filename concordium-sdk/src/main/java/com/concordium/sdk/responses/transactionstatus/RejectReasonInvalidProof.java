package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Proof that the baker owns relevant private keys is not valid.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonInvalidProof extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_PROOF;
    }
}
