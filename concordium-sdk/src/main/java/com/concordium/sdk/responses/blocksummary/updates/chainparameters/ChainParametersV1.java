package com.concordium.sdk.responses.blocksummary.updates.chainparameters;


import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
            @JsonUnwrapped PoolParametersV1 poolParametersV1
    ) {
        super(rewardParameters,
                microCCDPerEuro,
                foundationAccountIndex,
                accountCreationLimit,
                electionDifficulty,
                euroPerEnergy,
                poolParametersV1);
        this.mintPerPayday = mintPerPayday;
        this.poolOwnerCooldown = poolOwnerCooldown;
        this.rewardPeriodLength = rewardPeriodLength;
        this.delegatorCooldown = delegatorCooldown;
    }
}
