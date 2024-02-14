package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * At least one of the credentials was either malformed or its proof was incorrect.
 */
@ToString
public class RejectReasonInvalidCredentials extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_CREDENTIALS;
    }
}
