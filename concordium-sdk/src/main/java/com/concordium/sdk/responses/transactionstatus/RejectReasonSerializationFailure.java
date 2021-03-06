package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * Serialization of the body failed.
 */
@ToString
public class RejectReasonSerializationFailure extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SERIALIZATION_FAILURE;
    }
}
