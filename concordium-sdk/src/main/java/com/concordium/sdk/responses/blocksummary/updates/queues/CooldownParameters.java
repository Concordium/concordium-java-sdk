package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class CooldownParameters {

    /**
     * Baker extra cooldown in epochs (for versions < 4 only)
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

    @JsonCreator
    CooldownParameters(@JsonProperty("bakerExtraCooldownEpochs") long bakerExtraCooldownEpochs,
                       @JsonProperty("poolOwnerCooldown") long poolOwnerCooldown,
                       @JsonProperty("delegatorCooldown") long delegatorCooldown) {
        this.bakerExtraCooldownEpochs = bakerExtraCooldownEpochs;
        this.poolOwnerCooldown = poolOwnerCooldown;
        this.delegatorCooldown = delegatorCooldown;
    }
}
