package com.concordium.sdk.responses.blockitemsummary;

/**
 * The chain updates that are possible on the chain.
 * See {@link com.concordium.sdk.responses.chainparameters.ChainParametersV0} and {@link com.concordium.sdk.responses.chainparameters.ChainParametersV1}
 */
public enum UpdateType {
    /**
     * A protocol update.
     * These types of updates alter the protocol used on the chain.
     * See {@link com.concordium.sdk.responses.ProtocolVersion}
     */
    UPDATE_PROTOCOL,
    /**
     * The election difficulty determines the lottery power of
     * bakers on the chain and hence which baker wins in a certain slot.
     * This is only used in protocol versions 1-5.
     */
    UPDATE_ELECTION_DIFFICULTY,
    /**
     * An update that alters the euro per energy chain parameter.
     * This is used to peg the execution cost of transactions to the euro.
     */
    UPDATE_EURO_PER_ENERGY,
    /**
     * An update that alters the micro CCD per euro parameter.
     * This is used to peg the execution cost of transactions to the euro.
     */
    UPDATE_MICRO_CCD_PER_EURO,
    /**
     * An update that updates the registered foundation account on the chain.
     * A chain is initialized with a foundation account which receives a percentage of minted
     * CCD. This is to ensure that the concordium blockchain has sufficient resources to maintain the
     * chain and its ecosystem.
     */
    UPDATE_FOUNDATION_ACCOUNT,
    /**
     * An update that alters the actual mint distribution on the chain.
     * Only applicable in protocol versions 1-5.
     */
    UPDATE_MINT_DISTRIBUTION,
    /**
     * An update that alters the transaction fee distribution parameters on the chain.
     * This determines the transaction fees paid among the baker, the GAS account and the
     * foundation account.
     */
    UPDATE_TRANSACTION_FEE_DISTRIBUTION,
    /**
     * An update that alters the GAS rewards.
     * The GAS rewards determines how much the GAS account pays out
     * to the baker (pool).
     * Note that from protocol version 6 and onwards bakers are no longer
     * rewarded for including finalization proofs in a block.
     * This is since the consensus protocol changed from protocol version 6 and
     * finalization happens due to a different mechanic than previous.
     */
    UPDATE_GAS_REWARDS,
    /**
     * An update that alters what the required stake is in order
     * to be eligible for baking.
     */
    BAKER_STAKE_THRESHOLD_UPDATE,
    /**
     * An update that alters the pool parameters.
     * From protocol version 1-3 the parameters consisted solely of the
     * threshold required for being eligible to bake.
     * The parameters changed in protocol version 4 in order
     * to support baker pools and thus there are various parameters
     * determining how baker pools can behave.
     */
    UPDATE_POOL_PARAMETERS,
    /**
     * An anonymity revoker was registered on the chain.
     */
    ADD_ANONYMITY_REVOKER,
    /**
     * An identity provider was registered on the chain.
     */
    ADD_IDENTITY_PROVIDER,
    /**
     * Root keys were updated.
     */
    UPDATE_ROOT_KEYS,
    /**
     * Level 1 keys were updated.
     */
    UPDATE_LEVEL1_KEYS,
    /**
     * Level 2 keys were updated.
     * The level 2 keys can issue chain parameter updates
     * (if a sufficient number of signatures are collected)
     */
    UPDATE_LEVEL2_KEYS,
    /**
     * Update to the cooldown parameters.
     * Cooldown parameters determines the cooldown associated
     * with de-registering a baker or lowering their stake.
     * From protcol version 4 this also determines the cooldown for
     * delegators lowering their delegated stake.
     */
    UPDATE_COOLDOWN_PARAMETERS,
    /**
     * Update to the time parameters.
     * Introduced as part of protocol version 4 these parameters
     * determines the duration of the reward period i.e. the time between
     * paydays.
     * Paydays are special events that occur in blocks which pays out the
     * accumulated rewards during the reward period.
     */
    UPDATE_TIME_PARAMETERS,
    /**
     * Update the timeout parameters.
     * This update is possible from protocol version 6 and onwards.
     * These parameters determine how the duration of a round grows and shrinks
     * due to timeouts.
     */
    UPDATE_TIMEOUT_PARAMETERS,
    /**
     * Update to the minimum block time parameter.
     * Only applicable from protocol version 6 and onwards.
     * This determines the minimum duration between blocks.
     */
    UPDATE_MIN_BLOCK_TIME,
    /**
     * Update to the maximum energy that can be spent in a block.
     * Only applicable from protocol version 6 and onwards.
     */
    UPDATE_BLOCK_ENERGY_LIMIT,
    /**
     * Update to the {@link com.concordium.sdk.responses.chainparameters.FinalizationCommitteeParameters}
     * This determines how bakers are being selected for being member
     * of the finalization committee.
     * This parameter is only applicable from protocol version 6 and onwards.
     */
    UPDATE_FINALIZATION_COMMITTEE_PARAMETERS,
    /**
     * Update to the {@link com.concordium.sdk.responses.chainparameters.ValidatorScoreParameters},
     * parameters that govern validator suspension.
     * This parameter is only applicable from protocol version 8 and onwards.
     */
    VALIDATOR_SCORE_PARAMETERS,
    /**
     * A new protocol-level token (PLT) has been created.
     * This parameter is only applicable from protocol version 9 and onwards.
     * <br>
     * <b>This can't happen though, because CreatePLT operations are not enqueued, but happen immediately.</b>
     */
    CREATE_PLT,
}
