package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.UpdateDetails;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.TimeoutParameters;
import com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.chainparameters.*;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.time.Duration;
import java.util.Optional;

/**
 * Details of the different chain updates that
 * may occur on the chain.
 */
@EqualsAndHashCode(doNotUseGetters = true)
@Builder
@ToString
public class ChainUpdateDetails {
    private final Timestamp effectiveTime;

    private final UpdateType type;

    /**
     * The protocol update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_PROTOCOL}
     */
    private final ProtocolUpdate protocolUpdate;

    public Optional<ProtocolUpdate> getProtocolUpdate() {
        if (this.type == UpdateType.UPDATE_PROTOCOL) {
            return Optional.of(this.protocolUpdate);
        }
        return Optional.empty();
    }

    /**
     * The election difficulty update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_ELECTION_DIFFICULTY}
     */
    private final double electionDifficultyUpdate;

    public Optional<Double> getElectionDifficultyUpdate() {
        if (this.type == UpdateType.UPDATE_ELECTION_DIFFICULTY) {
            return Optional.of(this.electionDifficultyUpdate);
        }
        return Optional.empty();
    }

    /**
     * The euro per energy update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_EURO_PER_ENERGY}
     */
    private final Fraction euroPerEnergyUpdate;

    public Optional<Fraction> getEuroPerEnergy() {
        if (this.type == UpdateType.UPDATE_EURO_PER_ENERGY) {
            return Optional.of(this.euroPerEnergyUpdate);
        }
        return Optional.empty();
    }

    /**
     * The micro CCD per euro update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MICRO_CCD_PER_EURO}
     */
    private final Fraction microCCDPerEuroUpdate;

    public Optional<Fraction> getMicroCCDPerEuroUpdate() {
        if (this.type == UpdateType.UPDATE_MICRO_CCD_PER_EURO) {
            return Optional.of(this.microCCDPerEuroUpdate);
        }
        return Optional.empty();
    }

    /**
     * The foundation account update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_FOUNDATION_ACCOUNT}
     */
    private final AccountAddress foundationAccountUpdate;

    public Optional<AccountAddress> getFoundationAccountUpdate() {
        if (this.type == UpdateType.UPDATE_MICRO_CCD_PER_EURO) {
            return Optional.of(this.foundationAccountUpdate);
        }
        return Optional.empty();
    }

    /**
     * The mint distribution update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MINT_DISTRIBUTION}
     */
    private final MintDistribution mintDistributionUpdate;

    public Optional<MintDistribution> getMintDistributionUpdate() {
        if (this.type == UpdateType.UPDATE_MINT_DISTRIBUTION) {
            return Optional.of(this.mintDistributionUpdate);
        }
        return Optional.empty();
    }

    /**
     * The transaction fee distribution update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TRANSACTION_FEE_DISTRIBUTION}
     */
    private final TransactionFeeDistribution transactionFeeDistributionUpdate;

    public Optional<TransactionFeeDistribution> getTransactionFeeDistributionUpdate() {
        if (this.type == UpdateType.UPDATE_TRANSACTION_FEE_DISTRIBUTION) {
            return Optional.of(this.transactionFeeDistributionUpdate);
        }
        return Optional.empty();
    }

    /**
     * The GAS rewards update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_GAS_REWARDS}
     */
    private final GasRewards gasRewardsUpdate;

    public Optional<GasRewards> getGasRewardsUpdate() {
        if (this.type == UpdateType.UPDATE_GAS_REWARDS) {
            return Optional.of(this.gasRewardsUpdate);
        }
        return Optional.empty();
    }

    /**
     * The baker stake threshold update.
     * This is only non-null if the type is {@link UpdateType#BAKER_STAKE_THRESHOLD_UPDATE}
     */
    private final CCDAmount bakerStakeThresholdUpdate;

    public Optional<CCDAmount> getBakerStakeThresholdUpdate() {
        if (this.type == UpdateType.BAKER_STAKE_THRESHOLD_UPDATE) {
            return Optional.of(this.bakerStakeThresholdUpdate);
        }
        return Optional.empty();
    }

    /**
     * The pool parameters update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_POOL_PARAMETERS}
     */
    private final PoolParameters poolParametersUpdate;

    public Optional<PoolParameters> getPoolParametersUpdate() {
        if (this.type == UpdateType.UPDATE_POOL_PARAMETERS) {
            return Optional.of(this.poolParametersUpdate);
        }
        return Optional.empty();
    }

    /**
     * An anonymity revoker was added.
     * This is only non-null if the type is {@link UpdateType#ADD_ANONYMITY_REVOKER}
     */
    private final AnonymityRevokerInfo anonymityRevokerInfoUpdate;

    public Optional<AnonymityRevokerInfo> getAnonymityRevokerInfoUpdate() {
        if (this.type == UpdateType.ADD_ANONYMITY_REVOKER) {
            return Optional.of(this.anonymityRevokerInfoUpdate);
        }
        return Optional.empty();
    }

    /**
     * An identity provider was added.
     * This is only non-null if the type is {@link UpdateType#ADD_IDENTITY_PROVIDER}
     */
    private final IdentityProviderInfo identityProviderInfoUpdate;

    public Optional<IdentityProviderInfo> getIdentityProviderInfoUpdate() {
        if (this.type == UpdateType.ADD_IDENTITY_PROVIDER) {
            return Optional.of(this.identityProviderInfoUpdate);
        }
        return Optional.empty();
    }

    /**
     * Root keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_ROOT_KEYS}
     */
    private final RootKeysUpdate rootKeysUpdate;

    public Optional<RootKeysUpdate> getRootKeysUpdate() {
        if (this.type == UpdateType.UPDATE_ROOT_KEYS) {
            return Optional.of(this.rootKeysUpdate);
        }
        return Optional.empty();
    }

    /**
     * Level 1 keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_LEVEL1_KEYS}
     */
    private final Level1KeysUpdate level1KeysUpdate;

    public Optional<Level1KeysUpdate> getLevel1KeysUpdate() {
        if (this.type == UpdateType.UPDATE_LEVEL1_KEYS) {
            return Optional.of(this.level1KeysUpdate);
        }
        return Optional.empty();
    }

    /**
     * Level 2 keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_LEVEL2_KEYS}
     */
    private final Authorizations level2KeysUpdate;

    public Optional<Authorizations> getLevel2KeysUpdate() {
        if (this.type == UpdateType.UPDATE_LEVEL2_KEYS) {
            return Optional.of(this.level2KeysUpdate);
        }
        return Optional.empty();
    }

    /**
     * Cooldown parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_COOLDOWN_PARAMETERS}
     */
    private final CooldownParameter cooldownParametersUpdate;

    public Optional<CooldownParameter> getCooldownParametersUpdate() {
        if (this.type == UpdateType.UPDATE_COOLDOWN_PARAMETERS) {
            return Optional.of(this.cooldownParametersUpdate);
        }
        return Optional.empty();
    }

    /**
     * Time parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TIME_PARAMETERS}
     */
    private final TimeParameters timeParametersUpdate;

    public Optional<TimeParameters> getTimeParametersUpdate() {
        if (this.type == UpdateType.UPDATE_TIME_PARAMETERS) {
            return Optional.of(this.timeParametersUpdate);
        }
        return Optional.empty();
    }

    /**
     * Timeout parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TIMEOUT_PARAMETERS}
     */
    private final TimeoutParameters timeoutParametersUpdate;

    public Optional<TimeoutParameters> getTimeoutParametersUpdate() {
        if (this.type == UpdateType.UPDATE_TIMEOUT_PARAMETERS) {
            return Optional.of(this.timeoutParametersUpdate);
        }
        return Optional.empty();
    }

    /**
     * The minimum block time was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MIN_BLOCK_TIME}
     */
    private final Duration minBlockTimeUpdate;

    public Optional<Duration> getMinBlockTimeUpdate() {
        if (this.type == UpdateType.UPDATE_MIN_BLOCK_TIME) {
            return Optional.of(this.minBlockTimeUpdate);
        }
        return Optional.empty();
    }

    /**
     * The maximum energy a block can spend when executing was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_BLOCK_ENERGY_LIMIT}
     */
    private final Energy blockEnergyLimitUpdate;

    public Optional<Energy> getBlockEnergyLimitUpdate() {
        if (this.type == UpdateType.UPDATE_BLOCK_ENERGY_LIMIT) {
            return Optional.of(this.blockEnergyLimitUpdate);
        }
        return Optional.empty();
    }

    /**
     * The maximum energy a block can spend when executing was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_FINALIZATION_COMMITTEE_PARAMETERS}
     */
    private final FinalizationCommitteeParameters finalizationCommitteeParametersUpdate;

    public Optional<FinalizationCommitteeParameters> getFinalizationCommitteeParametersUpdate() {
        if (this.type == UpdateType.UPDATE_FINALIZATION_COMMITTEE_PARAMETERS) {
            return Optional.of(this.finalizationCommitteeParametersUpdate);
        }
        return Optional.empty();
    }

    public static ChainUpdateDetails from(UpdateDetails update) {
        val chainUpdateDetailsBuilder = ChainUpdateDetails
                .builder()
                .effectiveTime(Timestamp.newSeconds(update.getEffectiveTime().getValue()));
        val payload = update.getPayload();
        switch (payload.getPayloadCase()) {
            case PROTOCOL_UPDATE:
                val protocolUpdate = payload.getProtocolUpdate();
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_PROTOCOL)
                        .protocolUpdate(ProtocolUpdate.from(protocolUpdate))
                        .build();
                break;
            case ELECTION_DIFFICULTY_UPDATE:
                val electionDiffUpdate = payload.getElectionDifficultyUpdate();
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_ELECTION_DIFFICULTY)
                        .electionDifficultyUpdate(electionDiffUpdate.getValue().getPartsPerHundredThousand() / 100_000d);
                break;
            case EURO_PER_ENERGY_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_EURO_PER_ENERGY)
                        .euroPerEnergyUpdate(Fraction.from(payload.getEuroPerEnergyUpdate().getValue()));
                break;
            case MICRO_CCD_PER_EURO_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_MICRO_CCD_PER_EURO)
                        .microCCDPerEuroUpdate(Fraction.from(payload.getMicroCcdPerEuroUpdate().getValue()));
                break;
            case FOUNDATION_ACCOUNT_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_FOUNDATION_ACCOUNT)
                        .foundationAccountUpdate(AccountAddress.from(payload.getFoundationAccountUpdate()));
                break;
            case MINT_DISTRIBUTION_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_MINT_DISTRIBUTION)
                        .mintDistributionUpdate(MintDistributionCpV0.from(payload.getMintDistributionUpdate()));
                break;
            case TRANSACTION_FEE_DISTRIBUTION_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_TRANSACTION_FEE_DISTRIBUTION)
                        .transactionFeeDistributionUpdate(com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution.from(payload.getTransactionFeeDistributionUpdate()));
                break;
            case GAS_REWARDS_UPDATE:
            case GAS_REWARDS_CPV_2_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_GAS_REWARDS)
                        .gasRewardsUpdate(com.concordium.sdk.responses.chainparameters.GasRewards.from(payload.getGasRewardsUpdate()));
                break;
            case BAKER_STAKE_THRESHOLD_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.BAKER_STAKE_THRESHOLD_UPDATE)
                        .bakerStakeThresholdUpdate(CCDAmount.from(payload.getBakerStakeThresholdUpdate().getBakerStakeThreshold()));
                break;
            case ROOT_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_ROOT_KEYS)
                        .rootKeysUpdate(RootKeysUpdate.from(payload.getRootUpdate()));
                break;
            case LEVEL_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_LEVEL1_KEYS)
                        .level1KeysUpdate(Level1KeysUpdate.from(payload.getLevel1Update()));
                break;
            case ADD_ANONYMITY_REVOKER_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.ADD_ANONYMITY_REVOKER)
                        .anonymityRevokerInfoUpdate(AnonymityRevokerInfo.from(payload.getAddAnonymityRevokerUpdate()));
                break;
            case ADD_IDENTITY_PROVIDER_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.ADD_IDENTITY_PROVIDER)
                        .identityProviderInfoUpdate(IdentityProviderInfo.from(payload.getAddIdentityProviderUpdate()));
                break;
            case COOLDOWN_PARAMETERS_CPV_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_COOLDOWN_PARAMETERS);
                if (payload.hasCooldownParametersCpv1Update()) {
                    chainUpdateDetailsBuilder.cooldownParametersUpdate(CooldownParametersCpv1.from(payload.getCooldownParametersCpv1Update()));
                }
                break;
            case POOL_PARAMETERS_CPV_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_POOL_PARAMETERS);
                if (payload.hasPoolParametersCpv1Update()) {
                    chainUpdateDetailsBuilder.poolParametersUpdate(PoolParameters.from(payload.getPoolParametersCpv1Update()));
                }
                break;
            case TIME_PARAMETERS_CPV_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_TIME_PARAMETERS)
                        .timeParametersUpdate(TimeParameters.from(payload.getTimeParametersCpv1Update()));
                break;
            case MINT_DISTRIBUTION_CPV_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_MINT_DISTRIBUTION)
                        .mintDistributionUpdate(MintDistributionCpV1.from(payload.getMintDistributionCpv1Update()));
                break;
            case TIMEOUT_PARAMETERS_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_TIMEOUT_PARAMETERS)
                        .timeoutParametersUpdate(TimeoutParameters.from(payload.getTimeoutParametersUpdate()));
                break;
            case MIN_BLOCK_TIME_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_MIN_BLOCK_TIME)
                        .minBlockTimeUpdate(java.time.Duration.ofMillis(payload.getMinBlockTimeUpdate().getValue()));
                break;
            case BLOCK_ENERGY_LIMIT_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_BLOCK_ENERGY_LIMIT)
                        .blockEnergyLimitUpdate(com.concordium.sdk.requests.smartcontracts.Energy.from(payload.getBlockEnergyLimitUpdate()));
                break;
            case FINALIZATION_COMMITTEE_PARAMETERS_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_FINALIZATION_COMMITTEE_PARAMETERS)
                        .finalizationCommitteeParametersUpdate(FinalizationCommitteeParameters.from(payload.getFinalizationCommitteeParametersUpdate()));
                break;
            case PAYLOAD_NOT_SET:
                throw new IllegalArgumentException("Unrecognized chain update");

        }
        return chainUpdateDetailsBuilder.build();
    }

}
