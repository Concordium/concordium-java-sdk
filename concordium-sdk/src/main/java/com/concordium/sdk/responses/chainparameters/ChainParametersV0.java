package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Chain parameters for protocol versions 1 to 3.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
public class ChainParametersV0 extends ChainParameters {
    /**
     * The election difficulty parameter for the consensus lottery.
     */
    private final double electionDifficulty;

    /**
     * The euro per energy exchange rate.
     */
    private final Fraction euroPerEnergy;

    /**
     * The micro CCD per euro exchange rate.
     */
    private final Fraction microCCDPerEuro;

    /**
     * Extra number of epochs before stake is reduced, or
     * a baker is completely de-registered.
     */
    private final Epoch bakerCooldownEpochs;

    /**
     * The maximum limit of credential deployments per block.
     */
    private final int credentialsPerBlockLimit;

    /**
     * The current mint distribution.
     */
    private final MintDistributionCpV0 mintDistribution;

    /**
     * The current transaction fee distribution.
     */
    private final TransactionFeeDistribution transactionFeeDistribution;

    /**
     * The current gas rewards parameters.
     */
    private final GasRewards gasRewards;

    /**
     * The account address of the foundation account.
     */
    private final AccountAddress foundationAccount;

    /**
     * Minimum threshold required for being eligible for baking.
     * I.e. this is the minimum threshold that a baker is required to
     * allocate for baking when registering as a baker.
     */
    private final CCDAmount minimumThresholdForBaking;

    /**
     * The current root keys.
     * Root keys are allowed to do root updates.
     */
    private final HigherLevelKeys rootKeys;

    /**
     * The current level 1 keys.
     * Level 1 keys are allowed to do level 1 updates.
     */
    private final HigherLevelKeys level1Keys;

    /**
     * The current level 2 keys.
     * Level 2 are allowed to do chain parameter updates.
     */
    private final AuthorizationsV0 level2Keys;

    @Override
    public Version getVersion() {
        return Version.CPV0;
    }
}
