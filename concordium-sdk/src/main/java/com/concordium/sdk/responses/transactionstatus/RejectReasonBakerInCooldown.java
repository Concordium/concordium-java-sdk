package com.concordium.sdk.responses.transactionstatus;

public class RejectReasonBakerInCooldown extends RejectReasonContent {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.BAKER_IN_COOLDOWN;
    }
}
