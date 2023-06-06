package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.grpc.v2.AuthorizationsV0;
import com.concordium.grpc.v2.AuthorizationsV1;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * Level 2 update keys.
 * The set of keys authorized for chain updates,
 * together with access structures {@link Authorization} determining which keys are authorized for which update types.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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
     * Keys authorized for changing the MicroCCD per Euro rate.
     */
    private final Authorization microCCDPerEuro;

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
     * Note that this is null if and only if the chain (protocol version) does not support the update.
     */
    private final Authorization cooldownParameters;

    /**
     * Keys authorized for changing the time parameters.
     * Note that this is null if and only if the chain (protocol version) does not support the update.
     */
    private final Authorization timeParameters;

    /**
     * The set of keys authorized for chain updates.
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
                      @JsonProperty("poolParameters") Authorization poolParameters,
                      @JsonProperty("cooldownParameters") Authorization cooldownParameters,
                      @JsonProperty("timeParameters") Authorization timeParameters) {
        this.mintDistribution = mintDistribution;
        this.addAnonymityRevoker = addAnonymityRevoker;
        this.transactionFeeDistribution = transactionFeeDistribution;
        this.bakerStakeThreshold = bakerStakeThreshold;
        this.microCCDPerEuro = microGTUPerEuro;
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

    /**
     * Parses {@link AuthorizationsV0} to {@link Level2KeysUpdates}.
     * @param level2KeysUpdateV0 {@link AuthorizationsV0} returned by the GRPC V2 API.
     * @return parsed {@link Level2KeysUpdates}.
     */
    public static Level2KeysUpdates parse(AuthorizationsV0 level2KeysUpdateV0) {
        return setV0fields(level2KeysUpdateV0).build();
    }

    /**
     * Parses {@link AuthorizationsV1} to {@link Level2KeysUpdates}.
     * @param level2KeysUpdateV1 {@link AuthorizationsV1} returned by the GRPC V2 API.
     * @return parsed {@link Level2KeysUpdates}.
     */
    public static Level2KeysUpdates parse(AuthorizationsV1 level2KeysUpdateV1) {
        return setV0fields(level2KeysUpdateV1.getV0())
                .cooldownParameters(Authorization.parse(level2KeysUpdateV1.getParameterCooldown()))
                .timeParameters(Authorization.parse(level2KeysUpdateV1.getParameterTime()))
                .build();
    }

    /**
     * Helper method for setting the fields available in {@link AuthorizationsV0}.
     * @param level2KeysUpdateV0 {@link AuthorizationsV0} returned by the GRPC V2 API.
     * @return Builder for {@link Level2KeysUpdates} with fields availabe in {@link AuthorizationsV0} configured.
     */
    private static Level2KeysUpdatesBuilder setV0fields(AuthorizationsV0 level2KeysUpdateV0) {
        val keys = new ImmutableList.Builder<VerificationKey>();
        level2KeysUpdateV0.getKeysList().forEach(k -> keys.add(VerificationKey.parse(k)));
        return Level2KeysUpdates.builder()
                .verificationKeys(keys.build())
                .emergency(Authorization.parse(level2KeysUpdateV0.getEmergency()))
                .protocol(Authorization.parse(level2KeysUpdateV0.getProtocol()))
                .electionDifficulty(Authorization.parse(level2KeysUpdateV0.getParameterConsensus()))
                .euroPerEnergy(Authorization.parse(level2KeysUpdateV0.getParameterEuroPerEnergy()))
                .microCCDPerEuro(Authorization.parse(level2KeysUpdateV0.getParameterMicroCCDPerEuro()))
                .foundationAccount(Authorization.parse(level2KeysUpdateV0.getParameterFoundationAccount()))
                .mintDistribution(Authorization.parse(level2KeysUpdateV0.getParameterMintDistribution()))
                .transactionFeeDistribution(Authorization.parse(level2KeysUpdateV0.getParameterTransactionFeeDistribution()))
                .paramGASRewards(Authorization.parse(level2KeysUpdateV0.getParameterGasRewards()))
                .poolParameters(Authorization.parse(level2KeysUpdateV0.getPoolParameters()))
                .addAnonymityRevoker(Authorization.parse(level2KeysUpdateV0.getAddAnonymityRevoker()))
                .addIdentityProvider(Authorization.parse(level2KeysUpdateV0.getAddIdentityProvider()));

    }
}
