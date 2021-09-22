package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonRuntimeFailure extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.RUNTIME_FAILURE;
    }
}
