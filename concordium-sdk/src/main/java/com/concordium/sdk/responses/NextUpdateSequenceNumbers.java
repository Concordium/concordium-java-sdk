package com.concordium.sdk.responses;

import com.concordium.sdk.types.Nonce;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Update Sequence Numbers for Chain Parameters.
 */
@Builder
@EqualsAndHashCode
@ToString
public class NextUpdateSequenceNumbers {
    /**
     * Updates to the root keys.
     */
    private final Nonce rootKeys;

    /**
     * Updates to the level 1 keys.
     */
    private final Nonce level1Keys;

    /**
     * Updates to the level 2 keys.
     */
    private final Nonce level2Keys;

    /**
     * Protocol updates.
     */
    private final Nonce protocol;

    /**
     * Updates to the election difficulty parameter.
     */
    private final Nonce electionDifficulty;

    /**
     * Updates to the euro:energy exchange rate.
     */
    private final Nonce euroPerEnergy;

    /**
     * Updates to the CCD:EUR exchange rate.
     */
    private final Nonce microCcdPerEuro;

    /**
     * Updates to the foundation account.
     */
    private final Nonce foundationAccount;

    /**
     * Updates to the mint distribution.
     */
    private final Nonce mintDistribution;

    /**
     * Updates to the transaction fee distribution.
     */
    private final Nonce transactionFeesDistribution;

    /**
     * Updates to the GAS rewards.
     */
    private final Nonce gasRewards;

    /**
     * Updates pool parameters.
     */
    private final Nonce poolParameters;

    /**
     * Adds a new anonymity revoker.
     */
    private final Nonce addAnonymityRevoker;

    /**
     * Adds a new identity provider.
     */
    private final Nonce addIdentityProvider;

    /**
     * Updates to cooldown parameters for chain parameters version 1 introduced in protocol version 4.
     */
    private final Nonce cooldownParameters;

    /**
     * Updates to time parameters for chain parameters version 1 introduced in protocol version 4.
     */
    private final Nonce timeParameters;

    /**
     * Updates to the timeout parameters for chain parameters version 2 introduced in protocol version 6.
     */
    private final Nonce timeoutParameters;

    /**
     * Updates to the minimum block time parameter for chain parameters version 2 introduced in protocol version 6.
     */
    private final Nonce minBlockTime;

    /**
     * Updates to the maximum block energy limit parameter for chain parameters version 2 introduced in protocol version 6.
     */
    private final Nonce blockEnergyLimit;

    /**
     * Updates to the finalization committee parameters for chain parameters version 2 introduced in protocol version 6.
     */
    private final Nonce finalizationCommitteeParameters;

    /**
     * Updates to the validator score parameters for chain parameters version 3.
     */
    private final Nonce validatorScoreParameters;
}
