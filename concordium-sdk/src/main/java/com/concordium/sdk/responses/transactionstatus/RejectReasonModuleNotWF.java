package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonModuleNotWF extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_NOT_WF;
    }
}
