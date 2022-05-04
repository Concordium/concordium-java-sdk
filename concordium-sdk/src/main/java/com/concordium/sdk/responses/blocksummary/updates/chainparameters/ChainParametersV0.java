package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public final class ChainParametersV0 extends ChainParameters {

    /**
     * The cooldown (in epochs) that a baker is entering
     * when de-registering as a baker.
     */
    private final int bakerCooldownEpochs;

    /**
     * The minimum threshold of CCD required for participating in baking.
     */
    private final CCDAmount minimumThresholdForBaking;

    @JsonCreator
    ChainParametersV0(
                      @JsonProperty("rewardParameters") RewardParameters rewardParameters,
                      @JsonProperty("microGTUPerEuro") Fraction microCCDPerEuro,
                      @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
                      @JsonProperty("accountCreationLimit") int accountCreationLimit,
                      @JsonProperty("electionDifficulty") double electionDifficulty,
                      @JsonProperty("euroPerEnergy") Fraction euroPerEnergy,
                      @JsonProperty("bakerCooldownEpochs") int bakerCooldownEpochs,
                      @JsonProperty("minimumThresholdForBaking") CCDAmount minimumThresholdForBaking) {
        super(rewardParameters, microCCDPerEuro, foundationAccountIndex, accountCreationLimit, electionDifficulty, euroPerEnergy);
        this.bakerCooldownEpochs = bakerCooldownEpochs;
        this.minimumThresholdForBaking = minimumThresholdForBaking;
    }
}
