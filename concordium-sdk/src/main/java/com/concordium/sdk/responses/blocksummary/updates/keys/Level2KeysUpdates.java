package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.sdk.responses.blocksummary.updates.*;
import com.concordium.sdk.types.Nonce;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Level 2 update keys
 */
@Getter
@ToString
public final class Level2KeysUpdates {
    /**
     * Keys authorized to update mint distribution.
     */
    private final Authorization mintDistribution;

    /**
     * Keys authorized for adding anonymity revokers.
     */
    private final Authorization addAnonymityRevoker;

    /**
     * Keys authorized for changing the transaction fee distribution.
     */
    private final Authorization transactionFeeDistribution;

    /**
     * Keys authorized for changing the baker stake threshold.
     */
    private final Authorization bakerStakeThreshold;

    /**
     * Keys authorized for changing the micro per gtu rate.
     */
    private final Authorization microGTUPerEuro;

    /**
     * Keys authorized for changing the protocol.
     */
    private final Authorization protocol;

    /**
     * Keys authorized for adding a new identity provider.
     */
    private final Authorization addIdentityProvider;

    /**
     * Keys authorized for changing the gas rewards.
     */
    private final Authorization paramGASRewards;

    /**
     * Keys authorized for invoking emergency updates.
     */
    private final Authorization emergency;

    /**
     * Keys authorized for updating the foundation account.
     */
    private final Authorization foundationAccount;

    /**
     * Keys authorized for changing the election difficulty
     */
    private final Authorization electionDifficulty;

    /**
     * Keys authorized for changing the euro per energy rate.
     */
    private final Authorization euroPerEnergy;

    /**
     * Keys authorized for changing the pool parameters.
     */
    private final Authorization poolParameters;

    /**
     * Keys authorized for changing the cooldown parameters.
     */
    private final Authorization cooldownParameters;

    /**
     * Keys authorized for changing the time parameters.
     */
    private final Authorization timeParameters;

    /**
     * All keys authorized.
     */
    private final List<VerificationKey> verificationKeys;


    @JsonCreator
    Level2KeysUpdates(@JsonProperty("mintDistribution") Authorization mintDistribution,
                      @JsonProperty("addAnonymityRevoker") Authorization addAnonymityRevoker,
                      @JsonProperty("transactionFeeDistribution") Authorization transactionFeeDistribution,
                      @JsonProperty("bakerStakeThreshold") Authorization bakerStakeThreshold,
                      @JsonProperty("microGTUPerEuro") Authorization microGTUPerEuro,
                      @JsonProperty("protocol") Authorization protocol,
                      @JsonProperty("addIdentityProvider") Authorization addIdentityProvider,
                      @JsonProperty("paramGASRewards") Authorization paramGASRewards,
                      @JsonProperty("emergency") Authorization emergency,
                      @JsonProperty("keys") List<VerificationKey> verificationKeys,
                      @JsonProperty("foundationAccount") Authorization foundationAccount,
                      @JsonProperty("electionDifficulty") Authorization electionDifficulty,
                      @JsonProperty("euroPerEnergy") Authorization euroPerEnergy,
                      @JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                      @JsonProperty("poolParameters") Authorization poolParameters,
                      @JsonProperty("queue") List<Object> queue,
                      @JsonProperty("cooldownParameters") Authorization cooldownParameters,
                      @JsonProperty("timeParameters") Authorization timeParameters) {
        this.mintDistribution = mintDistribution;
        this.addAnonymityRevoker = addAnonymityRevoker;
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.bakerStakeThreshold = bakerStakeThreshold;
        this.microGTUPerEuro = microGTUPerEuro;
        this.protocol = protocol;
        this.addIdentityProvider = addIdentityProvider;
        this.paramGASRewards = paramGASRewards;
        this.emergency = emergency;
        this.verificationKeys = verificationKeys;
        this.foundationAccount = foundationAccount;
        this.electionDifficulty = electionDifficulty;
        this.euroPerEnergy = euroPerEnergy;
        this.poolParameters = poolParameters;
        this.cooldownParameters = cooldownParameters;
        this.timeParameters = timeParameters;
    }
}
