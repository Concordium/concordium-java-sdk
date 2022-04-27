package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class ChainParameters {
    private final CCDAmount minimumThresholdForBaking;
    private final RewardParameters rewardParameters;
    private final MicroGTUPerEuroUpdates microGTUPerEuroUpdates;
    private final int foundationAccountIndex;
    private final int accountCreationLimit;
    private final int bakerCooldownEpochs;
    private final double electionDifficulty;
    private final EuroPerEnergyUpdates euroPerEnergyUpdates;

    @JsonCreator
    ChainParameters(@JsonProperty("minimumThresholdForBaking") CCDAmount minimumThresholdForBaking,
                           @JsonProperty("rewardParameters") RewardParameters rewardParameters,
                           @JsonProperty("microGTUPerEuro") MicroGTUPerEuroUpdates microGTUPerEuroUpdates,
                           @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
                           @JsonProperty("accountCreationLimit") int accountCreationLimit,
                           @JsonProperty("bakerCooldownEpochs") int bakerCooldownEpochs,
                           @JsonProperty("electionDifficulty") double electionDifficulty,
                           @JsonProperty("euroPerEnergy") EuroPerEnergyUpdates euroPerEnergyUpdates) {
        this.minimumThresholdForBaking = minimumThresholdForBaking;
        this.rewardParameters = rewardParameters;
        this.microGTUPerEuroUpdates = microGTUPerEuroUpdates;
        this.foundationAccountIndex = foundationAccountIndex;
        this.accountCreationLimit = accountCreationLimit;
        this.bakerCooldownEpochs = bakerCooldownEpochs;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergyUpdates = euroPerEnergyUpdates;
    }
}
