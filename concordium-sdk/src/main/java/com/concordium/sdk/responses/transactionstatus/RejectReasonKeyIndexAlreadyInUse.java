package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonKeyIndexAlreadyInUse extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.KEY_INDEX_ALREADY_IN_USE;
    }
}
