package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonOutOfEnergy extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.OUT_OF_ENERGY;
    }
}
