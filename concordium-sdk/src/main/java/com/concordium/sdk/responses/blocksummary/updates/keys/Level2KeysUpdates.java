package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

/**
 * Level 2 update keys
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class Level2KeysUpdates {
    /**
     * Keys authorized to update mint distribution.
     */
    @JsonProperty("mintDistribution")
    private final Authorization mintDistribution;

    /**
     * Keys authorized for adding anonymity revokers.
     */
    @JsonProperty("addAnonymityRevoker")
    private final Authorization addAnonymityRevoker;

    /**
     * Keys authorized for changing the transaction fee distribution.
     */
    @JsonProperty("transactionFeeDistribution")
    private final Authorization transactionFeeDistribution;

    /**
     * Keys authorized for changing the micro per gtu rate.
     */
    @JsonProperty("microGTUPerEuro")
    private final Authorization microGTUPerEuro;

    /**
     * Keys authorized for changing the protocol.
     */
    @JsonProperty("protocol")
    private final Authorization protocol;

    /**
     * Keys authorized for adding a new identity provider.
     */
    @JsonProperty("addIdentityProvider")
    private final Authorization addIdentityProvider;

    /**
     * Keys authorized for changing the gas rewards.
     */
    @JsonProperty("paramGASRewards")
    private final Authorization paramGASRewards;

    /**
     * Keys authorized for invoking emergency updates.
     */
    @JsonProperty("emergency")
    private final Authorization emergency;

    /**
     * Keys authorized for updating the foundation account.
     */
    @JsonProperty("foundationAccount")
    private final Authorization foundationAccount;

    /**
     * Keys authorized for changing the election difficulty
     */
    @JsonProperty("electionDifficulty")
    private final Authorization electionDifficulty;

    /**
     * Keys authorized for changing the euro per energy rate.
     */
    @JsonProperty("euroPerEnergy")
    private final Authorization euroPerEnergy;

    /**
     * Keys authorized for changing the pool parameters.
     */
    @JsonProperty("poolParameters")
    private final Authorization poolParameters;

    /**
     * Keys authorized for changing the cooldown parameters.
     */
    @JsonProperty("cooldownParameters")
    private final Authorization cooldownParameters;

    /**
     * Keys authorized for changing the time parameters.
     */
    @JsonProperty("timeParameters")
    private final Authorization timeParameters;

    /**
     * All keys authorized.
     */
    @JsonProperty("keys")
    @Singular
    private final List<VerificationKey> verificationKeys;

    public Optional<Authorization> getCooldownParameters() {
        return Optional.ofNullable(cooldownParameters);
    }

    public Optional<Authorization> getTimeParameters() {
        return Optional.ofNullable(timeParameters);
    }
}
