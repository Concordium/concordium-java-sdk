package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Chain parameters used up till P4
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public final class ChainParametersV0 extends ChainParameters {

    /**
     * The cooldown (in epochs) that a baker is entering
     * when de-registering as a baker.
     */
    private final UInt64 bakerCooldownEpochs;

    @JsonCreator
    ChainParametersV0(
            @JsonProperty("rewardParameters") RewardParameters rewardParameters,
            @JsonProperty("microGTUPerEuro") Fraction microCCDPerEuro,
            @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
            @JsonProperty("accountCreationLimit") int accountCreationLimit,
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("euroPerEnergy") Fraction euroPerEnergy,
            @JsonUnwrapped PoolParametersV0 poolParametersV0,
            @JsonProperty("bakerCooldownEpochs") UInt64 bakerCooldownEpochs) {
        super(rewardParameters, microCCDPerEuro, foundationAccountIndex, accountCreationLimit, electionDifficulty, euroPerEnergy, poolParametersV0);
        this.bakerCooldownEpochs = bakerCooldownEpochs;
    }
}
