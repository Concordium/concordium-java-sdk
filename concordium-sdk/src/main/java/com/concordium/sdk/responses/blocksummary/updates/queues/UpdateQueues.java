package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.PoolParameters;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.PoolParametersV0;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.PoolParametersV1;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.GasRewards;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.MintDistribution;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.TransactionFeeDistribution;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level1KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level2KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.RootKeysUpdates;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

/**
 * Enqueued updates.
 */
// UpdateQueues must match
// https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/UpdateQueues.hs#L119
//
// Note. bakerStakeThreshold for versions < 4 and 'poolParameters' for versions > 3
//
@Getter
@ToString
public final class UpdateQueues<PoolParameters> {

    /**
     * Root keys updates enqueued.
     */
    private final EnqueuedUpdate<RootKeysUpdates> rootKeysUpdates;

    /**
     * Level1 keys updates enqueued.
     */
    private final EnqueuedUpdate<Level1KeysUpdates> level1KeysUpdates;

    /**
     * Level2 keys updates enqueued.
     */
    private final EnqueuedUpdate<Level2KeysUpdates> level2KeysUpdates;

    /**
     * Protocol updates enqueued.
     */
    private final EnqueuedUpdate<ProtocolUpdate> protocolUpdates;

    /**
     * Election difficulty updates enqueued.
     */
    private final EnqueuedUpdate<Double> electionDifficultyUpdates;

    /**
     * Euro:energy exchange rate updates enqueued.
     */
    private final EnqueuedUpdate<Fraction> euroPerEnergyUpdates;

    /**
     * GTU:euro exchange rate updates enqueued.
     */
    private final EnqueuedUpdate<Fraction> microGTUPerEuroUpdates;

    /**
     * Foundation updates enqueued.
     */
    private final EnqueuedUpdate<Integer> foundationAccountUpdates;

    /**
     * Mint distribution updates enqueued.
     */
    private final EnqueuedUpdate<MintDistribution> mintDistribution;

    /**
     * Transaction fee distribution updates enqueued.
     */
    private final EnqueuedUpdate<TransactionFeeDistribution> transactionFeeDistribution;

    /**
     * Rewards updates enqueued.
     */
    private final EnqueuedUpdate<GasRewards> gasRewards;

    /**
     * Baker stake threshold.
     */
    private final EnqueuedUpdate<CCDAmount> bakerStakeThreshold;

    /**
     * Pool parameter updates enqueued.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = PoolParametersV0.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = PoolParametersV1.class)
    })
    private final EnqueuedUpdate<PoolParameters> poolParameters;

    /**
     * New anonymity revokers enqueued.
     */
    private final EnqueuedUpdate<AnonymityRevokerInfo> addAnonymityRevokerUpdates;

    /**
     * New identity providers enqueued.
     */
    private final EnqueuedUpdate<IdentityProviderInfo> addIdentityProviderUpdates;

    /**
     * Cooldown parameter updates enqueued.
     */
    private final EnqueuedUpdate<CooldownParameters> cooldownParameters;

    /**
     * Time parameter updates enqueued.
     */
    private final EnqueuedUpdate<TimeParameters> timeParameters;

    @JsonCreator
    UpdateQueues(@JsonProperty("mintDistribution") EnqueuedUpdate<MintDistribution> mintDistribution,
                 @JsonProperty("rootKeys") EnqueuedUpdate<RootKeysUpdates> rootKeysUpdates,
                 @JsonProperty("addAnonymityRevoker") EnqueuedUpdate<AnonymityRevokerInfo> addAnonymityRevokerUpdates,
                 @JsonProperty("transactionFeeDistribution") EnqueuedUpdate<TransactionFeeDistribution> transactionFeeDistribution,
                 @JsonProperty("bakerStakeThreshold") EnqueuedUpdate<CCDAmount> bakerStakeThreshold,
                 @JsonProperty("level2Keys") EnqueuedUpdate<Level2KeysUpdates> level2KeysUpdates,
                 @JsonProperty("microGTUPerEuro") EnqueuedUpdate<Fraction> microGTUPerEuroUpdates,
                 @JsonProperty("protocol") EnqueuedUpdate<ProtocolUpdate> protocolUpdates,
                 @JsonProperty("addIdentityProvider") EnqueuedUpdate<IdentityProviderInfo> addIdentityProviderUpdates,
                 @JsonProperty("gasRewards") EnqueuedUpdate<GasRewards> gasRewards,
                 @JsonProperty("foundationAccount") EnqueuedUpdate<Integer> foundationAccountUpdates,
                 @JsonProperty("electionDifficulty") EnqueuedUpdate<Double> electionDifficultyUpdates,
                 @JsonProperty("euroPerEnergy") EnqueuedUpdate<Fraction> euroPerEnergyUpdates,
                 @JsonProperty("level1Keys") EnqueuedUpdate<Level1KeysUpdates> level1KeysUpdates,
                 @JsonProperty("cooldownParameters") EnqueuedUpdate<CooldownParameters> cooldownParameters,
                 @JsonProperty("timeParameters") EnqueuedUpdate<TimeParameters> timeParameters,
                 @JsonProperty("poolParameters") EnqueuedUpdate<PoolParameters> poolParameters) {
        this.mintDistribution = mintDistribution;
        this.rootKeysUpdates = rootKeysUpdates;
        this.addAnonymityRevokerUpdates = addAnonymityRevokerUpdates;
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.bakerStakeThreshold = bakerStakeThreshold;
        this.level2KeysUpdates = level2KeysUpdates;
        this.microGTUPerEuroUpdates = microGTUPerEuroUpdates;
        this.protocolUpdates = protocolUpdates;
        this.addIdentityProviderUpdates = addIdentityProviderUpdates;
        this.gasRewards = gasRewards;
        this.foundationAccountUpdates = foundationAccountUpdates;
        this.electionDifficultyUpdates = electionDifficultyUpdates;
        this.euroPerEnergyUpdates = euroPerEnergyUpdates;
        this.level1KeysUpdates = level1KeysUpdates;
        this.cooldownParameters = cooldownParameters;
        this.timeParameters = timeParameters;
        this.poolParameters = poolParameters;
    }
}
