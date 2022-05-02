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
    private final double mintPerPayday;
    private final int poolOwnerCooldown;
    private final double capitalBound;
    private final int rewardPeriodLength;
    private final double passiveBakingCommission;
    private final Fraction leverageBound;
    private final double passiveFinalizationCommission;
    private final int delegatorCooldown;
    private final double passiveTransactionCommission;
    private final Range bakingCommissionRange;
    private final Range finalizationCommissionRange;
    private final Range transactionCommissionRange;
    private final CCDAmount minimumEquityCapital;

    @JsonCreator
    ChainParameters(
            @JsonProperty("minimumThresholdForBaking") CCDAmount minimumThresholdForBaking,
            @JsonProperty("rewardParameters") RewardParameters rewardParameters,
            @JsonProperty("microGTUPerEuro") MicroGTUPerEuroUpdates microGTUPerEuroUpdates,
            @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
            @JsonProperty("accountCreationLimit") int accountCreationLimit,
            @JsonProperty("bakerCooldownEpochs") int bakerCooldownEpochs,
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("euroPerEnergy") EuroPerEnergyUpdates euroPerEnergyUpdates,
            @JsonProperty("mintPerPayday") double mintPerPayday,
            @JsonProperty("poolOwnerCooldown") int poolOwnerCooldown,
            @JsonProperty("capitalBound") double capitalBound,
            @JsonProperty("rewardPeriodLength") int rewardPeriodLength,
            @JsonProperty("passiveBakingCommission") double passiveBakingCommission,
            @JsonProperty("leverageBound") Fraction leverageBound,
            @JsonProperty("passiveFinalizationCommission") double passiveFinalizationCommission,
            @JsonProperty("delegatorCooldown") int delegatorCooldown,
            @JsonProperty("passiveTransactionCommission") double passiveTransactionCommission,
            @JsonProperty("bakingCommissionRange") Range bakingCommissionRange,
            @JsonProperty("finalizationCommissionRange") Range finalizationCommissionRange,
            @JsonProperty("transactionCommissionRange") Range transactionCommissionRange,
            @JsonProperty("minimumEquityCapital") CCDAmount minimumEquityCapital) {
        this.minimumThresholdForBaking = minimumThresholdForBaking;
        this.rewardParameters = rewardParameters;
        this.microGTUPerEuroUpdates = microGTUPerEuroUpdates;
        this.foundationAccountIndex = foundationAccountIndex;
        this.accountCreationLimit = accountCreationLimit;
        this.bakerCooldownEpochs = bakerCooldownEpochs;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergyUpdates = euroPerEnergyUpdates;
        this.mintPerPayday = mintPerPayday;
        this.poolOwnerCooldown = poolOwnerCooldown;
        this.capitalBound = capitalBound;
        this.rewardPeriodLength = rewardPeriodLength;
        this.passiveBakingCommission = passiveBakingCommission;
        this.leverageBound = leverageBound;
        this.passiveFinalizationCommission = passiveFinalizationCommission;
        this.delegatorCooldown = delegatorCooldown;
        this.passiveTransactionCommission = passiveTransactionCommission;
        this.bakingCommissionRange = bakingCommissionRange;
        this.finalizationCommissionRange = finalizationCommissionRange;
        this.transactionCommissionRange = transactionCommissionRange;
        this.minimumEquityCapital = minimumEquityCapital;
    }
}
