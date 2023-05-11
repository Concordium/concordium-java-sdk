package com.concordium.sdk.responses.transactionevent.updatepayloads;


/**
 * TODO explain better
 * Tagging interface for different kinds of Update Payloads. Payloads must be casted to a concrete type to access fields.
 */
public interface UpdatePayload {

    /**
     * Gets the {@link UpdateType} of the payload.
     * @return {@link UpdateType} of the payload.
     */
    UpdateType getType();

    /**
     * Parses {@link com.concordium.grpc.v2.UpdatePayload} to {@link UpdatePayload}.
     * @param payload {@link com.concordium.grpc.v2.UpdatePayload} returned by the GRPC V2 API.
     * @return parsed {@link UpdatePayload}.
     */
    static UpdatePayload parse(com.concordium.grpc.v2.UpdatePayload payload) {
        switch (payload.getPayloadCase()) {
            case PROTOCOL_UPDATE: return ProtocolUpdatePayload.parse(payload.getProtocolUpdate());
            case ELECTION_DIFFICULTY_UPDATE: return ElectionDifficultyUpdatePayload.parse(payload.getElectionDifficultyUpdate());
            case EURO_PER_ENERGY_UPDATE: return EuroPerEnergyUpdatePayload.parse(payload.getEuroPerEnergyUpdate());
            case MICRO_CCD_PER_EURO_UPDATE: return MicroCCDPerEuroUpdatePayload.parse(payload.getMicroCcdPerEuroUpdate());
            case FOUNDATION_ACCOUNT_UPDATE: return FoundationAccountUpdatePayload.parse(payload.getFoundationAccountUpdate());
            case MINT_DISTRIBUTION_UPDATE: return MintDistributionUpdatePayload.parse(payload.getMintDistributionUpdate());
            case TRANSACTION_FEE_DISTRIBUTION_UPDATE: return TransactionFeeDistributionUpdatePayload.parse(payload.getTransactionFeeDistributionUpdate());
            case GAS_REWARDS_UPDATE: return GasRewardsUpdatePayload.parse(payload.getGasRewardsUpdate());
            case BAKER_STAKE_THRESHOLD_UPDATE: return BakerStakeThresholdUpdatePayload.parse(payload.getBakerStakeThresholdUpdate());
            case ROOT_UPDATE: return RootUpdatePayload.parse(payload.getRootUpdate());
            case LEVEL_1_UPDATE: return Level1UpdatePayload.parse(payload.getLevel1Update());
            case ADD_ANONYMITY_REVOKER_UPDATE: return AddAnonymityRevokerUpdatePayload.parse(payload.getAddAnonymityRevokerUpdate());
            case ADD_IDENTITY_PROVIDER_UPDATE: return AddIdentityProviderUpdatePayload.parse(payload.getAddIdentityProviderUpdate());
            case COOLDOWN_PARAMETERS_CPV_1_UPDATE: return CooldownParametersCPV1UpdatePayload.parse(payload.getCooldownParametersCpv1Update());
            case POOL_PARAMETERS_CPV_1_UPDATE: return PoolParametersCPV1UpdatePayload.parse(payload.getPoolParametersCpv1Update());
            case TIME_PARAMETERS_CPV_1_UPDATE: return TimeParametersCPV1UpdatePayload.parse(payload.getTimeParametersCpv1Update());
            case MINT_DISTRIBUTION_CPV_1_UPDATE: return MintDistributionCPV1UpdatePayload.parse(payload.getMintDistributionCpv1Update());
            case GAS_REWARDS_CPV_2_UPDATE: return GasRewardsCPV2UpdatePayload.parse(payload.getGasRewardsCpv2Update());
            case TIMEOUT_PARAMETERS_UPDATE: return TimeoutParametersUpdatePayload.parse(payload.getTimeoutParametersUpdate());
            case MIN_BLOCK_TIME_UPDATE: return MinBlockTimeUpdatePayload.parse(payload.getMinBlockTimeUpdate());
            case BLOCK_ENERGY_LIMIT_UPDATE: return BlockEnergyLimitUpdatePayload.parse(payload.getBlockEnergyLimitUpdate());
            case FINALIZATION_COMMITTEE_PARAMETERS_UPDATE: return FinalizationCommitteeParametersUpdatePayload.parse(payload.getFinalizationCommitteeParametersUpdate());
            default: throw new IllegalArgumentException("Cannot parse payloadCase: " + payload.getPayloadCase());
        }
    }
}
