package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
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
    private final Fraction microGTUPerEuro;
    private final int foundationAccountIndex;
    private final int accountCreationLimit;
    private final int bakerCooldownEpochs;
    private final double electionDifficulty;
    private final Fraction euroPerEnergy;
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
            @JsonProperty("microGTUPerEuro") Fraction microGTUPerEuro,
            @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
            @JsonProperty("accountCreationLimit") int accountCreationLimit,
            @JsonProperty("bakerCooldownEpochs") int bakerCooldownEpochs,
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("euroPerEnergy") Fraction euroPerEnergy,
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
        this.microGTUPerEuro = microGTUPerEuro;
        this.foundationAccountIndex = foundationAccountIndex;
        this.accountCreationLimit = accountCreationLimit;
        this.bakerCooldownEpochs = bakerCooldownEpochs;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
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
