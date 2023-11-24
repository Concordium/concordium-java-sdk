package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Amounts accrued to accounts for each baked block.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
public final class BlockAccrueReward extends SpecialOutcome {
    /**
     * The total fees paid for transactions in the block.
     */
    private final CCDAmount transactionFees;

    /**
     * The old balance of the GAS account.
     */
    private final CCDAmount oldGASAccount;

    /**
     * The new balance of the GAS account.
     */
    private final CCDAmount newGASAccount;

    /**
     * The amount awarded to the baker.
     */
    private final CCDAmount bakerReward;

    /**
     * The amount awarded to the passive delegators.
     */
    private final CCDAmount passiveReward;

    /**
     * The amount awarded to the foundation.
     */
    private final CCDAmount foundationCharge;

    /**
     * The baker of the block, who will receive the award.
     */
    private final AccountIndex bakerId;
}
