package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The credential holder of the keys to be updated did not sign the transaction.
 */
@ToString
public class RejectReasonCredentialHolderDidNotSign extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.CREDENTIAL_HOLDER_DID_NOT_SIGN;
    }
}
