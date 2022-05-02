package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.MicroGTUPerEuroUpdates;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.GasRewards;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.MintDistribution;
import com.concordium.sdk.responses.blocksummary.updates.chainparameters.rewards.TransactionFeeDistribution;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level1KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.Level2KeysUpdates;
import com.concordium.sdk.responses.blocksummary.updates.keys.RootKeysUpdates;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * UpdateQueues must match
 * https://github.com/Concordium/concordium-base/blob/23c2f4f2fb524e60548f052d0422b567ec977822/haskell-src/Concordium/Types/UpdateQueues.hs#L119
 *
 * Note. bakerStakeThreshold for versions < 4 and 'poolParameters' for versions > 3
 */
@Getter
@ToString
public final class UpdateQueues {

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
    UpdateQueues(@JsonProperty("mintDistribution") MintDistribution mintDistribution,
                 @JsonProperty("rootKeys") RootKeysUpdates rootKeysUpdates,
                 @JsonProperty("addAnonymityRevoker") AnonymityRevokerInfo addAnonymityRevokerUpdates,
                 @JsonProperty("transactionFeeDistribution") TransactionFeeDistribution transactionFeeDistribution,
                 @JsonProperty("bakerStakeThreshold") CCDAmount bakerStakeThreshold,
                 @JsonProperty("level2Keys") Level2KeysUpdates level2KeysUpdates,
                 @JsonProperty("microGTUPerEuro") MicroGTUPerEuroUpdates microGTUPerEuroUpdates,
                 @JsonProperty("protocol") ProtocolUpdate protocolUpdates,
                 @JsonProperty("addIdentityProvider") IdentityProviderInfo addIdentityProviderUpdates,
                 @JsonProperty("gasRewards") GasRewards gasRewards,
                 @JsonProperty("foundationAccount") AccountAddress foundationAccountUpdates,
                 @JsonProperty("electionDifficulty") double electionDifficultyUpdates,
                 @JsonProperty("euroPerEnergy") double euroPerEnergyUpdates,
                 @JsonProperty("level1Keys") Level1KeysUpdates level1KeysUpdates,
                 @JsonProperty("cooldownParameters") CooldownParameters cooldownParameters,
                 @JsonProperty("timeParameters") TimeParameters timeParameters,
                 @JsonProperty("poolParameters") PoolParameters poolParameters) {
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
