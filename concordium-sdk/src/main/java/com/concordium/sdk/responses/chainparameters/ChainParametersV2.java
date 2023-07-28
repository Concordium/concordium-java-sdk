package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.Range;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.TimeoutParameters;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Chain parameters from protocol version 6 and onwards.
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
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

    public static com.concordium.sdk.responses.chainparameters.ChainParametersV2 from(com.concordium.grpc.v2.ChainParametersV2 v2Params) {
        return com.concordium.sdk.responses.chainparameters.ChainParametersV2
                .builder()
                .consensusParameters(ConsensusParameters
                        .builder()
                        .blockEnergyLimit(UInt64.from(v2Params.getConsensusParameters().getBlockEnergyLimit().getValue()))
                        .minBlockTime(java.time.Duration.ofMillis(v2Params.getConsensusParameters().getMinBlockTime().getValue()))
                        .timeoutParameters(TimeoutParameters
                                .builder()
                                .timeoutBase(java.time.Duration.ofMillis(v2Params.getConsensusParameters().getTimeoutParameters().getTimeoutBase().getValue()))
                                .timeoutIncrease(Fraction.from(v2Params.getConsensusParameters().getTimeoutParameters().getTimeoutIncrease()))
                                .timeoutDecrease(Fraction.from(v2Params.getConsensusParameters().getTimeoutParameters().getTimeoutDecrease()))
                                .build())
                        .build())
                .microCCDPerEuro(Fraction.from(v2Params.getMicroCcdPerEuro().getValue()))
                .euroPerEnergy(Fraction.from(v2Params.getEuroPerEnergy().getValue()))
                .mintDistribution(MintDistributionCpV1
                        .builder()
                        .bakingReward(PartsPerHundredThousand.from(v2Params.getMintDistribution().getBakingReward().getPartsPerHundredThousand()).asDouble())
                        .finalizationReward(PartsPerHundredThousand.from(v2Params.getMintDistribution().getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                        .build())
                .transactionFeeDistribution(com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution
                        .builder()
                        .allocatedForBaker(PartsPerHundredThousand.from(v2Params.getTransactionFeeDistribution().getBaker().getPartsPerHundredThousand()).asDouble())
                        .allocatedForGASAccount(PartsPerHundredThousand.from(v2Params.getTransactionFeeDistribution().getGasAccount().getPartsPerHundredThousand()).asDouble())
                        .build())
                .cooldownParameters(CooldownParametersCpv1
                        .builder()
                        .delegatorCooldown(v2Params.getCooldownParameters().getDelegatorCooldown().getValue())
                        .poolOwnerCooldown(v2Params.getCooldownParameters().getPoolOwnerCooldown().getValue())
                        .build())
                .credentialsPerBlockLimit(v2Params.getAccountCreationLimit().getValue())
                .foundationAccount(com.concordium.sdk.types.AccountAddress.from(v2Params.getFoundationAccount().getValue().toByteArray()))
                .timeParameters(TimeParameters
                        .builder()
                        .mintPerPayday(v2Params.getTimeParameters().getMintPerPayday().getMantissa() * Math.pow(10, -1 * v2Params.getTimeParameters().getMintPerPayday().getExponent()))
                        .rewardPeriodLength(v2Params.getTimeParameters().getRewardPeriodLength().getValue().getValue())
                        .build())
                .poolParameters(PoolParameters
                        .builder()
                        .passiveFinalizationCommission(PartsPerHundredThousand.from(v2Params.getPoolParameters().getPassiveFinalizationCommission().getPartsPerHundredThousand()).asDouble())
                        .passiveBakingCommission(PartsPerHundredThousand.from(v2Params.getPoolParameters().getPassiveBakingCommission().getPartsPerHundredThousand()).asDouble())
                        .passiveTransactionCommission(PartsPerHundredThousand.from(v2Params.getPoolParameters().getPassiveTransactionCommission().getPartsPerHundredThousand()).asDouble())
                        .transactionCommissionRange(Range.from(v2Params.getPoolParameters().getCommissionBounds().getTransaction()))
                        .finalizationCommissionRange(Range.from(v2Params.getPoolParameters().getCommissionBounds().getFinalization()))
                        .bakingCommissionRange(Range.from(v2Params.getPoolParameters().getCommissionBounds().getBaking()))
                        .minimumEquityCapital(CCDAmount.from(v2Params.getPoolParameters().getMinimumEquityCapital()))
                        .capitalBound(PartsPerHundredThousand.from(v2Params.getPoolParameters().getCapitalBound().getValue().getPartsPerHundredThousand()).asDouble())
                        .leverageBound(Fraction.from(v2Params.getPoolParameters().getLeverageBound().getValue()))
                        .build())
                .gasRewards(GasRewardsCpV2
                        .builder()
                        .accountCreation(PartsPerHundredThousand.from(v2Params.getGasRewards().getAccountCreation().getPartsPerHundredThousand()).asDouble())
                        .chainUpdate(PartsPerHundredThousand.from(v2Params.getGasRewards().getChainUpdate().getPartsPerHundredThousand()).asDouble())
                        .baker(PartsPerHundredThousand.from(v2Params.getGasRewards().getBaker().getPartsPerHundredThousand()).asDouble())
                        .build())
                .finalizationCommitteeParameters(FinalizationCommitteeParameters
                        .builder()
                        .minimumFinalizers(v2Params.getFinalizationCommitteeParameters().getMinimumFinalizers())
                        .maxFinalizers(v2Params.getFinalizationCommitteeParameters().getMaximumFinalizers())
                        .finalizerRelativeStakeThreshold(PartsPerHundredThousand.from(v2Params.getFinalizationCommitteeParameters().getFinalizerRelativeStakeThreshold().getPartsPerHundredThousand()).asDouble())
                        .build())
                .rootKeys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v2Params.getRootKeys()))
                .level1Keys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v2Params.getLevel1Keys()))
                .level2Keys(com.concordium.sdk.responses.chainparameters.AuthorizationsV1.from(v2Params.getLevel2Keys()))
                .build();
    }


    @Override
    public Version getVersion() {
        return Version.CPV2;
    }
}
