package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.transactions.CCDAmount;
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
     * The minimum threshold of CCD required for participating in baking.
     */
    private final CCDAmount minimumThresholdForBaking;

    @JsonCreator
    PoolParametersV0(@JsonProperty("bakerCooldownEpochs") CCDAmount minimumThresholdForBaking) {
        this.minimumThresholdForBaking = minimumThresholdForBaking;
    }
}
