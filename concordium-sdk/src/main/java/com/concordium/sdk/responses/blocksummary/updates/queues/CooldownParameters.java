package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Cooldown parameters
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class CooldownParameters {

    /**
     * Baker extra cooldown in epochs (for protocol versions < 4 only)
     */
    @JsonProperty("bakerExtraCooldownEpochs")
    private final long bakerExtraCooldownEpochs;

    /**
     * Pool owner cooldown in seconds
     */
    @JsonProperty("poolOwnerCooldown")
    private final long poolOwnerCooldown;

    /**
     * Delegator cooldown in seconds.
     */
    @JsonProperty("delegatorCooldown")
    private final long delegatorCooldown;
}
