package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.queues.CooldownParametersCpv1;
import com.concordium.sdk.responses.blocksummary.updates.queues.PoolParameters;
import com.concordium.sdk.responses.blocksummary.updates.queues.TimeParameters;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Chain parameters from protocol version 6 and onwards.
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public final class ChainParametersV2 extends ChainParameters {

    /**
     * Parameters related to the execution of the consensus protocol i.e., ConcordiumBFT.
     */
    private final ConsensusParameters consensusParameters;

    /**
     * The euro per energy exchange rate.
     */
    private final Fraction euroPerEnergy;

    /**
     * The micro CCD per euro exchange rate.
     */
    private final Fraction microCCDPerEuro;

    /**
     * Extra number of epochs before reduction in stake,
     * or the baker de-registration is completed.
     */
    private final CooldownParametersCpv1 cooldownParameters;

    /**
     * Time parameters indicates the mint rate and the reward
     * period length (i.e. time between paydays).
     */
    private final TimeParameters timeParameters;

    /**
     * The maximum limit of credential deployments per block.
     */
    private final int credentialsPerBlockLimit;

    /**
     * The mint distribution.
     */
    private final MintDistributionCpV1 mintDistribution;

    /**
     * The current transaction fee distribution.
     */
    private final TransactionFeeDistribution transactionFeeDistribution;

    /**
     * The current GAS reward parameters.
     */
    private final GasRewardsCpV2 gasRewards;

    /**
     * The account address of the foundation account.
     */
    private final AccountAddress foundationAccount;

    /**
     * Parameters that governs the baker pools.
     */
    private PoolParameters poolParameters;

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

    /**
     * The finalization committee parameters.
     */
    private final FinalizationCommitteeParameters finalizationCommitteeParameters;


    @Override
    public Version getVersion() {
        return Version.CPV2;
    }
}
