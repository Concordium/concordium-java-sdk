package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Payment distributed to a pool or passive delegators.
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public final class PaydayPoolReward extends SpecialOutcome {
    /**
     * The pool owner (passive delegators when 'Null')
     */
    private final Long poolOwner;

    /**
     * Accrued transaction fees for pool.
     */
    private final CCDAmount transactionFees;

    /**
     * Accrued baking rewards for pool.
     */
    private final CCDAmount bakerReward;

    /**
     * Accrued finalization rewards for pool.
     */
    private final CCDAmount finalizationReward;
}
