package com.concordium.sdk.responses.transactionstatus;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The change could not be made because the delegator is in cooldown.
 */
@ToString
@EqualsAndHashCode
public class RejectReasonDelegatorInCooldown extends RejectReason {
    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DELEGATOR_IN_COOLDOWN;
    }
}
