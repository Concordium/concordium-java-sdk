package com.concordium.sdk.responses.transactionevent.updatepayloads;

public enum UpdateType {

    /**
     * The protocol version was updated.
     */
    PROTOCOL_UPDATE,
    /**
     * The election difficulty was updated.
     */
    ELECTION_DIFFICULTY_UPDATE,
    /**
     * The euro per energy exchange rate was updated.
     */
    EURO_PER_ENERGY_UPDATE,
    /**
     * The microCCD per euro exchange rate was updated.
     */
    MICRO_CCD_PER_EURO_UPDATE,
    /**
     * The foundation account address was updated.
     */
    FOUNDATION_ACCOUNT_UPDATE,
    /**
     * The mint distribution was updated.
     */
    MINT_DISTRIBUTION_UPDATE,
    /**
     * The transaction fee distribution was updated.
     */
    TRANSACTION_FEE_DISTRIBUTION_UPDATE,
    /**
     * The gas rewards were updated.
     */
    GAS_REWARDS_UPDATE,
    /**
     * The minimum amount of CCD needed to become a baker was updated.
     */
    BAKER_STAKE_THRESHOLD_UPDATE,
    /**
     * The root keys were updated.
     */
    ROOT_UPDATE,
    /**
     * The level 1 keys were updated.
     */
    LEVEL_1_UPDATE,
    /**
     * An anonymity revoker was added.
     */
    ADD_ANONYMITY_REVOKERS_UPDATE,
    /**
     * An identity provider was added.
     */
    ADD_IDENTITY_PROVIDER_UPDATE,
    /**
     * The cooldown parameters were updated.
     */
    COOLDOWN_PARAMETERS_CPV_1_UPDATE,
    /**
     * The pool parameters were updated.
     */
    POOL_PARAMETERS_CPV_1_UPDATE,
    /**
     * The time parameters were updated.
     */
    TIME_PARAMETERS_CPV_1_UPDATE,
    /**
     * The mint distribution was updated.
     */
    MINT_DISTRIBUTION_CPV_1_UPDATE,
    /**
     * The gas rewards were updated (chain parameters version 2).
     */
    GAS_REWARDS_CPV_2_UPDATE,
    /**
     * The consensus timeouts were updated (chain parameters version 2).
     */
    TIMEOUT_PARAMETERS_UPDATE,
    /**
     * The minimum time between blocks was updated (chain parameters version 2).
     */
    MIN_BLOCK_TIME_UPDATE,
    /**
     * The block energy limit was updated (chain parameters version 2).
     */
    BLOCK_ENERGY_LIMIT_UPDATE,
    /**
     * The finalization committee parameters were updated (chain parameters version 2).
     */
    FINALIZATION_COMMITTEE_PARAMETERS_UPDATE;

    /**
     * TODO keep?
     * @param updatePayload
     * @return
     * @param <T>
     */
    public <T> T convert(UpdatePayload updatePayload) {
        if (this != updatePayload.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + updatePayload.getType());
        }
        return (T) updatePayload;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.UpdatePayload.PayloadCase} to {@link UpdateType}
     * @param payloadCase {@link com.concordium.grpc.v2.UpdatePayload.PayloadCase} returned by the GRPC V2 API
     * @return parsed {@link UpdateType}
     */
    public static UpdateType parse(com.concordium.grpc.v2.UpdatePayload.PayloadCase payloadCase) {
        switch (payloadCase) {
            case PROTOCOL_UPDATE: return PROTOCOL_UPDATE;
            case ELECTION_DIFFICULTY_UPDATE: return ELECTION_DIFFICULTY_UPDATE;
            case EURO_PER_ENERGY_UPDATE: return EURO_PER_ENERGY_UPDATE;
            case MICRO_CCD_PER_EURO_UPDATE: return MICRO_CCD_PER_EURO_UPDATE;
            case FOUNDATION_ACCOUNT_UPDATE: return FOUNDATION_ACCOUNT_UPDATE;
            case MINT_DISTRIBUTION_UPDATE: return MINT_DISTRIBUTION_UPDATE;
            case TRANSACTION_FEE_DISTRIBUTION_UPDATE: return TRANSACTION_FEE_DISTRIBUTION_UPDATE;
            case GAS_REWARDS_UPDATE: return GAS_REWARDS_UPDATE;
            case BAKER_STAKE_THRESHOLD_UPDATE: return BAKER_STAKE_THRESHOLD_UPDATE;
            case ROOT_UPDATE: return ROOT_UPDATE;
            case LEVEL_1_UPDATE: return LEVEL_1_UPDATE;
            case ADD_ANONYMITY_REVOKER_UPDATE: return ADD_ANONYMITY_REVOKERS_UPDATE;
            case ADD_IDENTITY_PROVIDER_UPDATE: return ADD_IDENTITY_PROVIDER_UPDATE;
            case COOLDOWN_PARAMETERS_CPV_1_UPDATE: return COOLDOWN_PARAMETERS_CPV_1_UPDATE;
            case POOL_PARAMETERS_CPV_1_UPDATE: return POOL_PARAMETERS_CPV_1_UPDATE;
            case TIME_PARAMETERS_CPV_1_UPDATE: return TIME_PARAMETERS_CPV_1_UPDATE;
            case MINT_DISTRIBUTION_CPV_1_UPDATE: return MINT_DISTRIBUTION_CPV_1_UPDATE;
            case GAS_REWARDS_CPV_2_UPDATE: return GAS_REWARDS_CPV_2_UPDATE;
            case TIMEOUT_PARAMETERS_UPDATE: return TIMEOUT_PARAMETERS_UPDATE;
            case MIN_BLOCK_TIME_UPDATE: return MIN_BLOCK_TIME_UPDATE;
            case BLOCK_ENERGY_LIMIT_UPDATE: return BLOCK_ENERGY_LIMIT_UPDATE;
            default:
                throw new IllegalArgumentException();
        }
    }

}
