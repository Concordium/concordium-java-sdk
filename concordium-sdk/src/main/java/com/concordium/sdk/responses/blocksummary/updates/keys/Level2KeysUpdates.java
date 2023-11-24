package com.concordium.sdk.responses.blocksummary.updates.keys;

import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * Level 2 update keys
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
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
    @Singular
    private final List<VerificationKey> verificationKeys;

    public Optional<Authorization> getCooldownParameters() {
        return Optional.ofNullable(cooldownParameters);
    }

    public Optional<Authorization> getTimeParameters() {
        return Optional.ofNullable(timeParameters);
    }
}
