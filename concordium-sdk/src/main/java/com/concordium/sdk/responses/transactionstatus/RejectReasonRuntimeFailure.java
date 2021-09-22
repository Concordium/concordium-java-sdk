package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonRuntimeFailure extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.RUNTIME_FAILURE;
    }
}
