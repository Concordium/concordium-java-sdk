package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Payment for a particular account.
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
@Builder
public final class PaydayAccountReward extends SpecialOutcome {

    /**
     * The account that got rewarded.
     */
    private final AccountAddress account;

    /**
     * The transaction fee reward at payday to the account.
     */
    private final CCDAmount transactionFees;

    /**
     * The baking reward at payday to the account.
     */
    private final CCDAmount bakerReward;

    /**
     * The finalization reward at payday to the account.
     */
    private final CCDAmount finalizationReward;
}
