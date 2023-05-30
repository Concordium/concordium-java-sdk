package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The account is not allowed to receive encrypted transfers because it has multiple credentials.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonNotAllowedToReceiveEncrypted extends RejectReason {

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_ALLOWED_TO_RECEIVE_ENCRYPTED;
    }
}
