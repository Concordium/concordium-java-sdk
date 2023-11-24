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
public final class CooldownParameters {

    /**
     * Baker extra cooldown in epochs (for protocol versions < 4 only)
     */
    private final long bakerExtraCooldownEpochs;

    /**
     * Pool owner cooldown in seconds
     */
    private final long poolOwnerCooldown;

    /**
     * Delegator cooldown in seconds.
     */
    private final long delegatorCooldown;
}
