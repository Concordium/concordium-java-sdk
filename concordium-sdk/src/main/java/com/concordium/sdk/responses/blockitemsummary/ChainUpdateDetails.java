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
import lombok.*;

import java.time.Duration;

/**
 * Details of a successful chain update.
 */
@EqualsAndHashCode(doNotUseGetters = true)
@Builder
@ToString(doNotUseGetters = true)
@Getter
public class ChainUpdateDetails {
    private final Timestamp effectiveTime;

    private final UpdateType type;

    /**
     * The protocol update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_PROTOCOL}
     */
    private final ProtocolUpdate protocolUpdate;

    /**
     * The election difficulty update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_ELECTION_DIFFICULTY}
     */
    private final double electionDifficultyUpdate;


    /**
     * The euro per energy update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_EURO_PER_ENERGY}
     */
    private final Fraction euroPerEnergyUpdate;

    /**
     * The micro CCD per euro update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MICRO_CCD_PER_EURO}
     */
    private final Fraction microCCDPerEuroUpdate;

    /**
     * The foundation account update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_FOUNDATION_ACCOUNT}
     */
    private final AccountAddress foundationAccountUpdate;

    /**
     * The mint distribution update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MINT_DISTRIBUTION}
     */
    private final MintDistribution mintDistributionUpdate;

    /**
     * The transaction fee distribution update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TRANSACTION_FEE_DISTRIBUTION}
     */
    private final TransactionFeeDistribution transactionFeeDistributionUpdate;

    /**
     * The GAS rewards update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_GAS_REWARDS}
     */
    private final GasRewards gasRewardsUpdate;

    /**
     * The baker stake threshold update.
     * This is only non-null if the type is {@link UpdateType#BAKER_STAKE_THRESHOLD_UPDATE}
     */
    private final CCDAmount bakerStakeThresholdUpdate;

    /**
     * The pool parameters update.
     * This is only non-null if the type is {@link UpdateType#UPDATE_POOL_PARAMETERS}
     */
    private final PoolParameters poolParametersUpdate;

    /**
     * An anonymity revoker was added.
     * This is only non-null if the type is {@link UpdateType#ADD_ANONYMITY_REVOKER}
     */
    private final AnonymityRevokerInfo anonymityRevokerInfoUpdate;

    /**
     * An identity provider was added.
     * This is only non-null if the type is {@link UpdateType#ADD_IDENTITY_PROVIDER}
     */
    private final IdentityProviderInfo identityProviderInfoUpdate;

    /**
     * Root keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_ROOT_KEYS}
     */
    private final RootKeysUpdate rootKeysUpdate;

    /**
     * Level 1 keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_LEVEL1_KEYS}
     */
    private final Level1KeysUpdate level1KeysUpdate;

    /**
     * Level 2 keys was altered.
     * This is only non-null if the type is {@link UpdateType#UPDATE_LEVEL2_KEYS}
     */
    private final Authorizations level2KeysUpdate;

    /**
     * Cooldown parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_COOLDOWN_PARAMETERS}
     */
    private final CooldownParameter cooldownParametersUpdate;

    /**
     * Time parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TIME_PARAMETERS}
     */
    private final TimeParameters timeParametersUpdate;

    /**
     * Timeout parameters was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_TIMEOUT_PARAMETERS}
     */
    private final TimeoutParameters timeoutParametersUpdate;

    /**
     * The minimum block time was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_MIN_BLOCK_TIME}
     */
    private final Duration minBlockTimeUpdate;

    /**
     * The maximum energy a block can spend when executing was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_BLOCK_ENERGY_LIMIT}
     */
    private final Energy blockEnergyLimitUpdate;

    /**
     * The maximum energy a block can spend when executing was changed.
     * This is only non-null if the type is {@link UpdateType#UPDATE_FINALIZATION_COMMITTEE_PARAMETERS}
     */
    private final FinalizationCommitteeParameters finalizationCommitteeParametersUpdate;

    /**
     * Parameters that govern validator suspension were changed.
     * This is only non-null if the type is {@link UpdateType#VALIDATOR_SCORE_PARAMETERS}
     */
    private final ValidatorScoreParameters validatorScoreParametersUpdate;

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
                        .type(UpdateType.UPDATE_COOLDOWN_PARAMETERS)
                        .cooldownParametersUpdate(CooldownParametersCpv1.from(payload.getCooldownParametersCpv1Update()));
                break;
            case POOL_PARAMETERS_CPV_1_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.UPDATE_POOL_PARAMETERS).
                        poolParametersUpdate(PoolParameters.from(payload.getPoolParametersCpv1Update()));
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
            case VALIDATOR_SCORE_PARAMETERS_UPDATE:
                chainUpdateDetailsBuilder
                        .type(UpdateType.VALIDATOR_SCORE_PARAMETERS)
                        .validatorScoreParametersUpdate(ValidatorScoreParameters.from(payload.getValidatorScoreParametersUpdate()));
                break;
            case CREATE_PLT_UPDATE:
                throw new IllegalStateException("This can't happen. CreatePLT operations are not enqueued, but happen immediately");
            case PAYLOAD_NOT_SET:
                throw new IllegalArgumentException("Unrecognized chain update");

        }
        return chainUpdateDetailsBuilder.build();
    }

}
