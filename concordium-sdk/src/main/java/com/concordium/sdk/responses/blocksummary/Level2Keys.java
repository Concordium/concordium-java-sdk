package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class Level2Keys {
    private final MintDistribution mintDistribution;
    private final AddAnonymityRevoker addAnonymityRevoker;
    private final TransactionFeeDistribution transactionFeeDistribution;
    private final BakerStakeThreshold bakerStakeThreshold;
    private final MicroGTUPerEuro microGTUPerEuro;
    private final Protocol protocol;
    private final AddIdentityProvider addIdentityProvider;
    private final ParamGASRewards paramGASRewards;
    private final Emergency emergency;
    private final List<Key> keys;
    private final FoundationAccount foundationAccount;
    private final ElectionDifficulty electionDifficulty;
    private final EuroPerEnergy euroPerEnergy;
    private final int nextSequenceNumber;
    private final List<Object> queue;

    @JsonCreator
    Level2Keys(@JsonProperty("mintDistribution") MintDistribution mintDistribution,
               @JsonProperty("addAnonymityRevoker") AddAnonymityRevoker addAnonymityRevoker,
               @JsonProperty("transactionFeeDistribution") TransactionFeeDistribution transactionFeeDistribution,
               @JsonProperty("bakerStakeThreshold") BakerStakeThreshold bakerStakeThreshold,
               @JsonProperty("microGTUPerEuro") MicroGTUPerEuro microGTUPerEuro,
               @JsonProperty("protocol") Protocol protocol,
               @JsonProperty("addIdentityProvider") AddIdentityProvider addIdentityProvider,
               @JsonProperty("paramGASRewards") ParamGASRewards paramGASRewards,
               @JsonProperty("emergency") Emergency emergency,
               @JsonProperty("keys") List<Key> keys,
               @JsonProperty("foundationAccount") FoundationAccount foundationAccount,
               @JsonProperty("electionDifficulty") ElectionDifficulty electionDifficulty,
               @JsonProperty("euroPerEnergy") EuroPerEnergy euroPerEnergy,
               @JsonProperty("nextSequenceNumber") int nextSequenceNumber,
               @JsonProperty("queue") List<Object> queue) {
        this.mintDistribution = mintDistribution;
        this.addAnonymityRevoker = addAnonymityRevoker;
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.bakerStakeThreshold = bakerStakeThreshold;
        this.microGTUPerEuro = microGTUPerEuro;
        this.protocol = protocol;
        this.addIdentityProvider = addIdentityProvider;
        this.paramGASRewards = paramGASRewards;
        this.emergency = emergency;
        this.keys = keys;
        this.foundationAccount = foundationAccount;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
        this.nextSequenceNumber = nextSequenceNumber;
        this.queue = queue;
    }
}
