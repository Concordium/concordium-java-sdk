package com.concordium.sdk.responses.blocksummary.updates.chainparameters;


import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Chain parameters used from P4 and onwards.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public final class ChainParametersV1 extends ChainParameters {

    /**
     * Mint rate per payday (as a proportion of the extant supply).
     */
    private final double mintPerPayday;

    /**
     * Number of seconds that pool owners must cooldown
     * when reducing their equity capital or closing the pool.
     * This is measured in seconds.
     */
    private final long poolOwnerCooldown;

    /**
     * Length of a reward period (a number of epochs).
     */
    private final long rewardPeriodLength;

    /**
     * Number of seconds that a delegator must cooldown
     * when reducing their delegated stake.
     * This is measured in seconds.
     */
    private final long delegatorCooldown;

    /**
     * The range of allowed finalization commissions.
     */
    private final double passiveFinalizationCommission;

    /**
     * The range of allowed baker commissions.
     */
    private final double passiveBakingCommission;

    /**
     * Commission rates charged for passive delegation.
     */
    private final double passiveTransactionCommission;

    /**
     * The range of allowed finalization commissions.
     */
    private final Range finalizationCommissionRange;

    /**
     * The range of allowed baker commissions.
     */
    private final Range bakingCommissionRange;

    /**
     * The range of allowed transaction commissions.
     */
    private final Range transactionCommissionRange;

    /**
     * Minimum equity capital required for a new baker.
     */
    private final CCDAmount minimumEquityCapital;

    /**
     * A bound on the relative share of the total staked capital that a baker can have as its stake.
     * This is required to be greater than 0.
     */
    private final double capitalBound;

    /**
     * The maximum leverage that a baker can have as a ratio of total stake
     * to equity capital.
     */
    private final Fraction leverageBound;


    @JsonCreator
    ChainParametersV1(
            @JsonProperty("rewardParameters") RewardParameters rewardParameters,
            @JsonProperty("microGTUPerEuro") Fraction microCCDPerEuro,
            @JsonProperty("foundationAccountIndex") int foundationAccountIndex,
            @JsonProperty("accountCreationLimit") int accountCreationLimit,
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("euroPerEnergy") Fraction euroPerEnergy,
            @JsonProperty("mintPerPayday") double mintPerPayday,
            @JsonProperty("poolOwnerCooldown") long poolOwnerCooldown,
            @JsonProperty("rewardPeriodLength") long rewardPeriodLength,
            @JsonProperty("delegatorCooldown") long delegatorCooldown,
            @JsonProperty("passiveTransactionCommission") double passiveTransactionCommission,
            @JsonProperty("bakingCommissionRange") Range bakingCommissionRange,
            @JsonProperty("finalizationCommissionRange") Range finalizationCommissionRange,
            @JsonProperty("transactionCommissionRange") Range transactionCommissionRange,
            @JsonProperty("minimumEquityCapital") CCDAmount minimumEquityCapital,
            @JsonProperty("passiveBakingCommission") double passiveBakingCommission,
            @JsonProperty("leverageBound") Fraction leverageBound,
            @JsonProperty("passiveFinalizationCommission") double passiveFinalizationCommission,
            @JsonProperty("capitalBound") double capitalBound
    ) {
        super(rewardParameters,
                microCCDPerEuro,
                foundationAccountIndex,
                accountCreationLimit,
                electionDifficulty,
                euroPerEnergy);
        this.mintPerPayday = mintPerPayday;
        this.poolOwnerCooldown = poolOwnerCooldown;
        this.rewardPeriodLength = rewardPeriodLength;
        this.delegatorCooldown = delegatorCooldown;
        this.passiveFinalizationCommission = passiveFinalizationCommission;
        this.passiveBakingCommission = passiveBakingCommission;
        this.passiveTransactionCommission = passiveTransactionCommission;
        this.finalizationCommissionRange = finalizationCommissionRange;
        this.bakingCommissionRange = bakingCommissionRange;
        this.transactionCommissionRange = transactionCommissionRange;
        this.minimumEquityCapital = minimumEquityCapital;
        this.capitalBound = capitalBound;
        this.leverageBound = leverageBound;
    }
}
