package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInvalidAccountThreshold extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_ACCOUNT_THRESHOLD;
    }
}
