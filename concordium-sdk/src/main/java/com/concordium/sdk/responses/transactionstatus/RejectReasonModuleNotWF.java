package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonModuleNotWF extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.MODULE_NOT_WF;
    }
}
