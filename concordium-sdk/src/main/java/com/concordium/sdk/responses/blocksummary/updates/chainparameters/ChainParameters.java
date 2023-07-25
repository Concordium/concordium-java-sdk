package com.concordium.sdk.responses.blocksummary.updates.chainparameters;

import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.RewardParameters;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Chain parameters common to {@link ChainParametersV0} and {@link ChainParametersV1}.
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class ChainParameters {

    /**
     * Election difficulty
     */
    private final double electionDifficulty;

    /**
     * The euro / energy rate
     */
    private final Fraction euroPerEnergy;

    /**
     * The micro CCD / euro rate
     */
    private final Fraction microCCDPerEuro;

    /**
     * The maximum allowed account creations per block.
     */
    private final int accountCreationLimit;

    /**
     * The reward parameters
     * See {@link RewardParameters}
     */
    private final RewardParameters rewardParameters;

    /**
     * AccountIndex of the foundation account.
     */
    private final int foundationAccountIndex;

    ChainParameters(
            RewardParameters rewardParameters,
            Fraction microCCDPerEuro,
            int foundationAccountIndex,
            int accountCreationLimit,
            double electionDifficulty,
            Fraction euroPerEnergy) {
        this.rewardParameters = rewardParameters;
        this.microCCDPerEuro = microCCDPerEuro;
        this.foundationAccountIndex = foundationAccountIndex;
        this.accountCreationLimit = accountCreationLimit;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
    }
}
