package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class UpdateQueues {
    private final MintDistributionUpdates mintDistributionUpdates;
    private final RootKeysUpdates rootKeysUpdates;
    private final AddAnonymityRevokerUpdates addAnonymityRevokerUpdates;
    private final TransactionFeeDistributionUpdates transactionFeeDistributionUpdates;
    private final BakerStakeThresholdUpdates bakerStakeThresholdUpdates;
    private final Level2KeysUpdates level2KeysUpdates;
    private final MicroGTUPerEuroUpdates microGTUPerEuroUpdates;
    private final ProtocolUpdates protocolUpdates;
    private final AddIdentityProviderUpdates addIdentityProviderUpdates;
    private final GasRewardsUpdates gasRewards;
    private final FoundationAccountUpdates foundationAccountUpdates;
    private final ElectionDifficultyUpdates electionDifficultyUpdates;
    private final EuroPerEnergyUpdates euroPerEnergyUpdates;
    private final Level1KeysUpdates level1KeysUpdates;

    @JsonCreator
    UpdateQueues(@JsonProperty("mintDistribution") MintDistributionUpdates mintDistributionUpdates,
                 @JsonProperty("rootKeys") RootKeysUpdates rootKeysUpdates,
                 @JsonProperty("addAnonymityRevoker") AddAnonymityRevokerUpdates addAnonymityRevokerUpdates,
                 @JsonProperty("transactionFeeDistribution") TransactionFeeDistributionUpdates transactionFeeDistributionUpdates,
                 @JsonProperty("bakerStakeThreshold") BakerStakeThresholdUpdates bakerStakeThresholdUpdates,
                 @JsonProperty("level2Keys") Level2KeysUpdates level2KeysUpdates,
                 @JsonProperty("microGTUPerEuro") MicroGTUPerEuroUpdates microGTUPerEuroUpdates,
                 @JsonProperty("protocol") ProtocolUpdates protocolUpdates,
                 @JsonProperty("addIdentityProvider") AddIdentityProviderUpdates addIdentityProviderUpdates,
                 @JsonProperty("gasRewards") GasRewardsUpdates gasRewards,
                 @JsonProperty("foundationAccount") FoundationAccountUpdates foundationAccountUpdates,
                 @JsonProperty("electionDifficulty") ElectionDifficultyUpdates electionDifficultyUpdates,
                 @JsonProperty("euroPerEnergy") EuroPerEnergyUpdates euroPerEnergyUpdates,
                 @JsonProperty("level1Keys") Level1KeysUpdates level1KeysUpdates) {
        this.mintDistributionUpdates = mintDistributionUpdates;
        this.rootKeysUpdates = rootKeysUpdates;
        this.addAnonymityRevokerUpdates = addAnonymityRevokerUpdates;
        this.transactionFeeDistributionUpdates = transactionFeeDistributionUpdates;
        this.bakerStakeThresholdUpdates = bakerStakeThresholdUpdates;
        this.level2KeysUpdates = level2KeysUpdates;
        this.microGTUPerEuroUpdates = microGTUPerEuroUpdates;
        this.protocolUpdates = protocolUpdates;
        this.addIdentityProviderUpdates = addIdentityProviderUpdates;
        this.gasRewards = gasRewards;
        this.foundationAccountUpdates = foundationAccountUpdates;
        this.electionDifficultyUpdates = electionDifficultyUpdates;
        this.euroPerEnergyUpdates = euroPerEnergyUpdates;
        this.level1KeysUpdates = level1KeysUpdates;
    }
}
