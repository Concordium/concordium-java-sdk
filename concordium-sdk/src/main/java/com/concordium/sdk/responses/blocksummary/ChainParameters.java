package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class ChainParameters {
    private final String minimumThresholdForBaking;
    private final RewardParameters rewardParameters;
    private final MicroGTUPerEuro microGTUPerEuro;
    private final int foundationAccountIndex;
    private final int accountCreationLimit;
    private final int bakerCooldownEpochs;
    private final double electionDifficulty;
    private final EuroPerEnergy euroPerEnergy;

    @JsonCreator
    ChainParameters(@JsonProperty("minimumThresholdForBaking") String minimumThresholdForBaking,
                           @JsonProperty("rewardParameters") RewardParameters rewardParameters,
                           @JsonProperty("microGTUPerEuro") MicroGTUPerEuro microGTUPerEuro,
                           @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
                           @JsonProperty("accountCreationLimit") int accountCreationLimit,
                           @JsonProperty("bakerCooldownEpochs") int bakerCooldownEpochs,
                           @JsonProperty("electionDifficulty") double electionDifficulty,
                           @JsonProperty("euroPerEnergy") EuroPerEnergy euroPerEnergy) {
        this.minimumThresholdForBaking = minimumThresholdForBaking;
        this.rewardParameters = rewardParameters;
        this.microGTUPerEuro = microGTUPerEuro;
        this.foundationAccountIndex = foundationAccountIndex;
        this.accountCreationLimit = accountCreationLimit;
        this.bakerCooldownEpochs = bakerCooldownEpochs;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
    }
}
