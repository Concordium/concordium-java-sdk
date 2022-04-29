package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * Account is not allowed to have multiple credentials because it contains a non-zero encrypted transfer.
 */
@ToString
public class RejectReasonNotAllowedMultipleCredentials extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_MULTIPLE_CREDENTIALS;
    }
}
