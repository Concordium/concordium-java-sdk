package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Payment distributed to a pool or passive delegators.
 */
@Getter
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

    @JsonCreator
    PaydayPoolReward(
            @JsonProperty("poolOwner") Long poolOwner,
            @JsonProperty("transactionFees") CCDAmount transactionFees,
            @JsonProperty("bakerReward") CCDAmount bakerReward,
            @JsonProperty("finalizationReward") CCDAmount finalizationReward) {
        this.poolOwner = poolOwner;
        this.transactionFees = transactionFees;
        this.bakerReward = bakerReward;
        this.finalizationReward = finalizationReward;
    }
}
