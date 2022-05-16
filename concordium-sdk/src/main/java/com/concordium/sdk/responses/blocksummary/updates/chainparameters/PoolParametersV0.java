package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class PoolParametersV0 extends PoolParameters {

    /**
     * The cooldown (in epochs) that a baker is entering
     * when de-registering as a baker.
     */
    private final UInt64 bakerCooldownEpochs;

    @JsonCreator
    PoolParametersV0(@JsonProperty("bakerCooldownEpochs") long bakerCooldownEpochs) {
        this.bakerCooldownEpochs = UInt64.from(bakerCooldownEpochs);
    }
}
