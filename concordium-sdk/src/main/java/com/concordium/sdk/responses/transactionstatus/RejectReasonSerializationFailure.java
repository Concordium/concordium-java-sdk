package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonSerializationFailure extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.SERIALIZATION_FAILURE;
    }
}
