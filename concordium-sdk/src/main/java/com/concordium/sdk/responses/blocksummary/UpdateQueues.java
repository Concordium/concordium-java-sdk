package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class UpdateQueues {
    private final MintDistribution mintDistribution;
    private final RootKeys rootKeys;
    private final AddAnonymityRevoker addAnonymityRevoker;
    private final TransactionFeeDistribution transactionFeeDistribution;
    private final BakerStakeThreshold bakerStakeThreshold;
    private final Level2Keys level2Keys;
    private final MicroGTUPerEuro microGTUPerEuro;
    private final Protocol protocol;
    private final AddIdentityProvider addIdentityProvider;
    private final GasRewards2 gasRewards;
    private final FoundationAccount foundationAccount;
    private final ElectionDifficulty electionDifficulty;
    private final EuroPerEnergy euroPerEnergy;
    private final Level1Keys level1Keys;

    @JsonCreator
    UpdateQueues(@JsonProperty("mintDistribution") MintDistribution mintDistribution,
                 @JsonProperty("rootKeys") RootKeys rootKeys,
                 @JsonProperty("addAnonymityRevoker") AddAnonymityRevoker addAnonymityRevoker,
                 @JsonProperty("transactionFeeDistribution") TransactionFeeDistribution transactionFeeDistribution,
                 @JsonProperty("bakerStakeThreshold") BakerStakeThreshold bakerStakeThreshold,
                 @JsonProperty("level2Keys") Level2Keys level2Keys,
                 @JsonProperty("microGTUPerEuro") MicroGTUPerEuro microGTUPerEuro,
                 @JsonProperty("protocol") Protocol protocol,
                 @JsonProperty("addIdentityProvider") AddIdentityProvider addIdentityProvider,
                 @JsonProperty("gasRewards") GasRewards2 gasRewards,
                 @JsonProperty("foundationAccount") FoundationAccount foundationAccount,
                 @JsonProperty("electionDifficulty") ElectionDifficulty electionDifficulty,
                 @JsonProperty("euroPerEnergy") EuroPerEnergy euroPerEnergy,
                 @JsonProperty("level1Keys") Level1Keys level1Keys) {
        this.mintDistribution = mintDistribution;
        this.rootKeys = rootKeys;
        this.addAnonymityRevoker = addAnonymityRevoker;
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.bakerStakeThreshold = bakerStakeThreshold;
        this.level2Keys = level2Keys;
        this.microGTUPerEuro = microGTUPerEuro;
        this.protocol = protocol;
        this.addIdentityProvider = addIdentityProvider;
        this.gasRewards = gasRewards;
        this.foundationAccount = foundationAccount;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
        this.level1Keys = level1Keys;
    }
}
