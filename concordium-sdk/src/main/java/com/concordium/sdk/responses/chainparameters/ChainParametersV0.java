package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Chain parameters for protocol versions 1 to 3.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
@AllArgsConstructor
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
    private final CooldownInEpochs bakerCooldownEpochs;

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

    @JsonCreator
    ChainParametersV0(
            @JsonProperty("rewardParameters") RewardParameters rewardParameters,
            @JsonProperty("microGTUPerEuro") Fraction microCCDPerEuro,
            @JsonProperty("accountCreationLimit") int accountCreationLimit,
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("euroPerEnergy") Fraction euroPerEnergy,
            @JsonProperty("minimumThresholdForBaking") CCDAmount bakerStakeThreshold,
            @JsonProperty("bakerCooldownEpochs") long bakerCooldownEpochs,
            @JsonProperty("foundationAccountIndex") AccountIndex foundationAccountIndex) {
        this.euroPerEnergy = euroPerEnergy;
        this.microCCDPerEuro = microCCDPerEuro;
        this.credentialsPerBlockLimit = accountCreationLimit;
        this.electionDifficulty = electionDifficulty;
        this.minimumThresholdForBaking = bakerStakeThreshold;
        this.bakerCooldownEpochs = CooldownInEpochs.from(bakerCooldownEpochs);
        this.gasRewards = rewardParameters.getGasRewards();
        this.mintDistribution = (MintDistributionCpV0) rewardParameters.getMintDistribution();
        this.transactionFeeDistribution = rewardParameters.getTransactionFeeDistribution();
        // these are not returned in the grpcv1 api.
        this.foundationAccount = null;
        this.rootKeys = null;
        this.level1Keys = null;
        this.level2Keys = null;
    }

    public static ChainParametersV0 from(com.concordium.grpc.v2.ChainParametersV0 v0Params) {
        return com.concordium.sdk.responses.chainparameters.ChainParametersV0
                .builder()
                .electionDifficulty(PartsPerHundredThousand.from(v0Params.getElectionDifficulty().getValue().getPartsPerHundredThousand()).asDouble())
                .microCCDPerEuro(Fraction.from(v0Params.getMicroCcdPerEuro().getValue()))
                .euroPerEnergy(Fraction.from(v0Params.getEuroPerEnergy().getValue()))
                .transactionFeeDistribution(com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution
                        .builder()
                        .allocatedForBaker(PartsPerHundredThousand.from(v0Params.getTransactionFeeDistribution().getBaker().getPartsPerHundredThousand()).asDouble())
                        .allocatedForGASAccount(PartsPerHundredThousand.from(v0Params.getTransactionFeeDistribution().getGasAccount().getPartsPerHundredThousand()).asDouble())
                        .build())
                .bakerCooldownEpochs(CooldownInEpochs.from(v0Params.getBakerCooldownEpochs().getValue()))
                .credentialsPerBlockLimit(v0Params.getAccountCreationLimit().getValue())
                .foundationAccount(com.concordium.sdk.types.AccountAddress.from(v0Params.getFoundationAccount().getValue().toByteArray()))
                .minimumThresholdForBaking(CCDAmount.from(v0Params.getMinimumThresholdForBaking()))
                .mintDistribution(MintDistributionCpV0.from(v0Params.getMintDistribution()))
                .gasRewards(com.concordium.sdk.responses.chainparameters.GasRewards
                        .builder()
                        .accountCreation(PartsPerHundredThousand.from(v0Params.getGasRewards().getAccountCreation().getPartsPerHundredThousand()).asDouble())
                        .chainUpdate(PartsPerHundredThousand.from(v0Params.getGasRewards().getChainUpdate().getPartsPerHundredThousand()).asDouble())
                        .finalizationProof(PartsPerHundredThousand.from(v0Params.getGasRewards().getFinalizationProof().getPartsPerHundredThousand()).asDouble())
                        .baker(PartsPerHundredThousand.from(v0Params.getGasRewards().getBaker().getPartsPerHundredThousand()).asDouble())
                        .build())
                .rootKeys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v0Params.getRootKeys()))
                .level1Keys(com.concordium.sdk.responses.chainparameters.HigherLevelKeys.from(v0Params.getLevel1Keys()))
                .level2Keys(com.concordium.sdk.responses.chainparameters.AuthorizationsV0.from(v0Params.getLevel2Keys()))
                .build();
    }

    @Override
    public Version getVersion() {
        return Version.CPV0;
    }
}
