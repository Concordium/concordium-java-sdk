package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonSerializationFailure extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SERIALIZATION_FAILURE;
    }
}
