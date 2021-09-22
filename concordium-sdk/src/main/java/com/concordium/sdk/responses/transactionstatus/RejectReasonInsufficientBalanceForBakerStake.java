package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonInsufficientBalanceForBakerStake extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INSUFFICIENT_BALANCE_FOR_BAKER_STAKE;
    }
}
