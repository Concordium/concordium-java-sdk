package com.concordium.sdk.responses.transactionstatus;

import lombok.ToString;

/**
 * The change could not be made because the baker is in cooldown for another change
 */
@ToString
public class RejectReasonBakerInCooldown extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.BAKER_IN_COOLDOWN;
    }
}
