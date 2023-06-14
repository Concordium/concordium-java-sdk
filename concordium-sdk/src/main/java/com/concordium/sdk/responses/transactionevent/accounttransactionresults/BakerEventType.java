package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.sdk.responses.transactionstatus.*;

/**
 * Baker event types used with the GRPCv2 API.
 */
public enum BakerEventType {
    /**
     * A baker was added.
     * This event type corresponds to the concrete event {@link BakerAddedResult}.
     */
    BAKER_ADDED,
    /**
     * A baker was removed.
     * This event type corresponds to the concrete event {@link BakerRemovedResult}.
     */
    BAKER_REMOVED,
    /**
     * Baker stake increased.
     * This event type corresponds to the concrete event {@link BakerStakeIncreasedResult}.
     */
    BAKER_STAKE_INCREASED,
    /**
     * Baker stake decreased.
     * This event type corresponds to the concrete event {@link BakerStakeDecreasedResult}.
     */
    BAKER_STAKE_DECREASED,
    /**
     * The baker's setting for restaking earnings was updated.
     * This event type corresponds to the concrete event {@link BakerSetRestakeEarningsResult}.
     */
    BAKER_RESTAKE_EARNINGS_UPDATED,
    /**
     * A baker's keys were updated.
     * This event type corresponds to the concrete event {@link BakerKeysUpdatedResult}.
     */
    BAKER_KEYS_UPDATED,
    /**
     * The baker updated its {@link OpenStatus}.
     * This event type corresponds to the concrete event {@link BakerSetOpenStatus}.
     */
    BAKER_SET_OPEN_STATUS,
    /**
     * Updated metadata URL for a baker pool.
     * This event type corresponds to the concrete event {@link BakerSetMetadataURL}.
     */
    BAKER_SET_METADATA_URL,
    /**
     * Updated transaction fee commission for a baker pool.
     * This event type corresponds to the concrete event {@link BakerSetTransactionFeeCommission}.
     */
    BAKER_SET_TRANSACTION_FEE_COMMISSION,
    /**
     * Updated baking reward commission for a baker pool.
     * This event type corresponds to the concrete event {@link BakerSetBakingRewardCommission}.
     */
    BAKER_SET_BAKING_REWARD_COMMISSION,
    /**
     * Updated finalization reward commission for a baker pool.
     * This event type corresponds to the concrete event {@link BakerSetFinalizationRewardCommission}.
     */
    BAKER_SET_FINALIZATION_REWARD_COMMISSION
}
