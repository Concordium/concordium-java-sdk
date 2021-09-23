package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

@ToString
public class RejectReasonBakerInCooldown extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.BAKER_IN_COOLDOWN;
    }
}
