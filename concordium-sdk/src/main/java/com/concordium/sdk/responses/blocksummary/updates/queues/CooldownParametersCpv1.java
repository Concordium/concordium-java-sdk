package com.concordium.sdk.responses.blocksummary.updates.queues;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Cooldown parameters
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CooldownParametersCpv1 {
    /**
     * Pool owner cooldown in seconds
     */
    private final long poolOwnerCooldown;

    /**
     * Delegator cooldown in seconds.
     */
    private final long delegatorCooldown;
}
