package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The account is not allowed to send encrypted transfers (or transfer from/to public to/from encrypted).
 */
@ToString
@EqualsAndHashCode
public class RejectReasonNotAllowedToHandleEncrypted extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_HANDLE_ENCRYPTED;
    }
}
