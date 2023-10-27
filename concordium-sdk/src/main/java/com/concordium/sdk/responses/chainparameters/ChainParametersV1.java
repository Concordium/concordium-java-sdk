package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.Range;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Chain parameters for protocol versions 4 to 5.
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class ChainParametersV1 extends ChainParameters {

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
     * The current gas rewards parameters.
     */
    private final GasRewards gasRewards;

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

    @JsonCreator
    ChainParametersV1(
            @JsonProperty("rewardParameters") RewardParameters rewardParameters,
            @JsonProperty("microGTUPerEuro") Fraction microCCDPerEuro,
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
            @JsonProperty("capitalBound") double capitalBound,
            @JsonProperty("foundationAccountIndex") AccountIndex foundationAccountIndex
    ) {
        this.mintDistribution = (MintDistributionCpV1) rewardParameters.getMintDistribution();
        this.gasRewards = rewardParameters.getGasRewards();
        this.transactionFeeDistribution = rewardParameters.getTransactionFeeDistribution();
        this.microCCDPerEuro = microCCDPerEuro;
        this.credentialsPerBlockLimit = accountCreationLimit;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
        this.cooldownParameters = CooldownParametersCpv1
                .builder()
                .delegatorCooldown(delegatorCooldown)
                .poolOwnerCooldown(poolOwnerCooldown)
                .build();
        this.timeParameters = TimeParameters
                .builder()
                .mintPerPayday(mintPerPayday)
                .rewardPeriodLength(rewardPeriodLength)
                .build();
        this.poolParameters = PoolParameters
                .builder()
                .passiveTransactionCommission(passiveTransactionCommission)
                .bakingCommissionRange(bakingCommissionRange)
                .finalizationCommissionRange(finalizationCommissionRange)
                .transactionCommissionRange(transactionCommissionRange)
                .minimumEquityCapital(minimumEquityCapital)
                .passiveBakingCommission(passiveBakingCommission)
                .leverageBound(leverageBound)
                .passiveFinalizationCommission(passiveFinalizationCommission)
                .capitalBound(capitalBound)
                .build();
        // these are not returned by the json api
        this.foundationAccount = null;
        this.rootKeys = null;
        this.level1Keys = null;
        this.level2Keys = null;

    }

    public static ChainParametersV1 from(com.concordium.grpc.v2.ChainParametersV1 v1Params) {
        return ChainParametersV1
                .builder()
                .electionDifficulty(PartsPerHundredThousand.from(v1Params.getElectionDifficulty().getValue().getPartsPerHundredThousand()).asDouble())
                .microCCDPerEuro(Fraction.from(v1Params.getMicroCcdPerEuro().getValue()))
                .euroPerEnergy(Fraction.from(v1Params.getEuroPerEnergy().getValue()))
                .mintDistribution(MintDistributionCpV1
                        .builder()
                        .bakingReward(PartsPerHundredThousand.from(v1Params.getMintDistribution().getBakingReward().getPartsPerHundredThousand()).asDouble())
                        .finalizationReward(PartsPerHundredThousand.from(v1Params.getMintDistribution().getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                        .build())
                .transactionFeeDistribution(com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution
                        .builder()
                        .allocatedForBaker(PartsPerHundredThousand.from(v1Params.getTransactionFeeDistribution().getBaker().getPartsPerHundredThousand()).asDouble())
                        .allocatedForGASAccount(PartsPerHundredThousand.from(v1Params.getTransactionFeeDistribution().getGasAccount().getPartsPerHundredThousand()).asDouble())
                        .build())
                .cooldownParameters(CooldownParametersCpv1
                        .builder()
                        .delegatorCooldown(v1Params.getCooldownParameters().getDelegatorCooldown().getValue())
                        .poolOwnerCooldown(v1Params.getCooldownParameters().getPoolOwnerCooldown().getValue())
                        .build())
                .credentialsPerBlockLimit(v1Params.getAccountCreationLimit().getValue())
                .foundationAccount(com.concordium.sdk.types.AccountAddress.from(v1Params.getFoundationAccount().getValue().toByteArray()))
                .timeParameters(TimeParameters
                        .builder()
                        .mintPerPayday(v1Params.getTimeParameters().getMintPerPayday().getMantissa() * Math.pow(10, -1 * v1Params.getTimeParameters().getMintPerPayday().getExponent()))
                        .rewardPeriodLength(v1Params.getTimeParameters().getRewardPeriodLength().getValue().getValue())
                        .build())
                .poolParameters(PoolParameters
                        .builder()
                        .passiveFinalizationCommission(PartsPerHundredThousand.from(v1Params.getPoolParameters().getPassiveFinalizationCommission().getPartsPerHundredThousand()).asDouble())
                        .passiveBakingCommission(PartsPerHundredThousand.from(v1Params.getPoolParameters().getPassiveBakingCommission().getPartsPerHundredThousand()).asDouble())
                        .passiveTransactionCommission(PartsPerHundredThousand.from(v1Params.getPoolParameters().getPassiveTransactionCommission().getPartsPerHundredThousand()).asDouble())
                        .transactionCommissionRange(Range.from(v1Params.getPoolParameters().getCommissionBounds().getTransaction()))
                        .finalizationCommissionRange(Range.from(v1Params.getPoolParameters().getCommissionBounds().getFinalization()))
                        .bakingCommissionRange(Range.from(v1Params.getPoolParameters().getCommissionBounds().getBaking()))
                        .minimumEquityCapital(CCDAmount.from(v1Params.getPoolParameters().getMinimumEquityCapital()))
                        .capitalBound(PartsPerHundredThousand.from(v1Params.getPoolParameters().getCapitalBound().getValue().getPartsPerHundredThousand()).asDouble())
                        .leverageBound(Fraction.from(v1Params.getPoolParameters().getLeverageBound().getValue()))
                        .build())
                .gasRewards(com.concordium.sdk.responses.chainparameters.GasRewards
                        .builder()
                        .accountCreation(PartsPerHundredThousand.from(v1Params.getGasRewards().getAccountCreation().getPartsPerHundredThousand()).asDouble())
                        .chainUpdate(PartsPerHundredThousand.from(v1Params.getGasRewards().getChainUpdate().getPartsPerHundredThousand()).asDouble())
                        .finalizationProof(PartsPerHundredThousand.from(v1Params.getGasRewards().getFinalizationProof().getPartsPerHundredThousand()).asDouble())
                        .baker(PartsPerHundredThousand.from(v1Params.getGasRewards().getBaker().getPartsPerHundredThousand()).asDouble())
                        .build())
                .rootKeys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v1Params.getRootKeys()))
                .level1Keys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v1Params.getLevel1Keys()))
                .level2Keys(com.concordium.sdk.responses.chainparameters.AuthorizationsV1.from(v1Params.getLevel2Keys()))
                .build();
    }


    @Override
    public Version getVersion() {
        return Version.CPV1;
    }
}
