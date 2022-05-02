package com.concordium.sdk.responses.blocksummary;

import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

//todo: a bunch of these should be just 'AccessStructure' types.
@Getter
@ToString
public final class Level2KeysUpdates {
    private final MintDistributionUpdates mintDistributionUpdates;
    private final AddAnonymityRevokerUpdates addAnonymityRevokerUpdates;
    private final TransactionFeeDistributionUpdates transactionFeeDistributionUpdates;
    private final BakerStakeThresholdUpdates bakerStakeThresholdUpdates;
    private final MicroGTUPerEuroUpdates microGTUPerEuroUpdates;
    private final ProtocolUpdates protocolUpdates;
    private final AddIdentityProviderUpdates addIdentityProviderUpdates;
    private final ParamGASRewards paramGASRewards;
    private final Emergency emergency;
    private final List<Key> keys;
    private final FoundationAccountUpdates foundationAccountUpdates;
    private final ElectionDifficultyUpdates electionDifficultyUpdates;
    private final EuroPerEnergyUpdates euroPerEnergyUpdates;
    private final Nonce nextSequenceNumber;
    private final List<Object> queue;
    private final PoolParameters poolParameters;
    private final CooldownParameters cooldownParameters;
    private final TimeParameters timeParameters;

    @JsonCreator
    Level2KeysUpdates(@JsonProperty("mintDistribution") MintDistributionUpdates mintDistributionUpdates,
                      @JsonProperty("addAnonymityRevoker") AddAnonymityRevokerUpdates addAnonymityRevokerUpdates,
                      @JsonProperty("transactionFeeDistribution") TransactionFeeDistributionUpdates transactionFeeDistributionUpdates,
                      @JsonProperty("bakerStakeThreshold") BakerStakeThresholdUpdates bakerStakeThresholdUpdates,
                      @JsonProperty("microGTUPerEuro") MicroGTUPerEuroUpdates microGTUPerEuroUpdates,
                      @JsonProperty("protocol") ProtocolUpdates protocolUpdates,
                      @JsonProperty("addIdentityProvider") AddIdentityProviderUpdates addIdentityProviderUpdates,
                      @JsonProperty("paramGASRewards") ParamGASRewards paramGASRewards,
                      @JsonProperty("emergency") Emergency emergency,
                      @JsonProperty("keys") List<Key> keys,
                      @JsonProperty("foundationAccount") FoundationAccountUpdates foundationAccountUpdates,
                      @JsonProperty("electionDifficulty") ElectionDifficultyUpdates electionDifficultyUpdates,
                      @JsonProperty("euroPerEnergy") EuroPerEnergyUpdates euroPerEnergyUpdates,
                      @JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                      @JsonProperty("poolParameters") PoolParameters poolParameters,
                      @JsonProperty("queue") List<Object> queue,
                      @JsonProperty("cooldownParameters") CooldownParameters cooldownParameters,
                      @JsonProperty("timeParameters") TimeParameters timeParameters) {
        this.mintDistributionUpdates = mintDistributionUpdates;
        this.addAnonymityRevokerUpdates = addAnonymityRevokerUpdates;
        this.transactionFeeDistributionUpdates = transactionFeeDistributionUpdates;
        this.bakerStakeThresholdUpdates = bakerStakeThresholdUpdates;
        this.microGTUPerEuroUpdates = microGTUPerEuroUpdates;
        this.protocolUpdates = protocolUpdates;
        this.addIdentityProviderUpdates = addIdentityProviderUpdates;
        this.paramGASRewards = paramGASRewards;
        this.emergency = emergency;
        this.keys = keys;
        this.foundationAccountUpdates = foundationAccountUpdates;
        this.electionDifficultyUpdates = electionDifficultyUpdates;
        this.euroPerEnergyUpdates = euroPerEnergyUpdates;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
        this.poolParameters = poolParameters;
        this.cooldownParameters = cooldownParameters;
        this.timeParameters = timeParameters;
    }
}
