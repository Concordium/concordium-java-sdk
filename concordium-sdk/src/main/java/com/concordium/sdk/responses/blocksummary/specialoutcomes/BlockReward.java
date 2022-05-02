package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Disbursement of fees from a block between the GAS account,
 * the baker, and the foundation. It should always be that:
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
public final class BlockReward extends SpecialOutcome {

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
     * The amount awarded to the foundation.
     */
    private final CCDAmount foundationCharge;

    /**
     * The baker of the block, who receives the award.
     */
    private final AccountAddress baker;

    /**
     * The foundation account.
     */
    private final AccountAddress foundationAccount;

    BlockReward(
            @JsonProperty("transactionFees") CCDAmount transactionFees,
            @JsonProperty("oldGASAccount") CCDAmount oldGASAccount,
            @JsonProperty("newGASAccount") CCDAmount newGASAccount,
            @JsonProperty("bakerReward") CCDAmount bakerReward,
            @JsonProperty("foundationCharge") CCDAmount foundationCharge,
            @JsonProperty("baker") AccountAddress baker,
            @JsonProperty("foundationAccount") AccountAddress foundationAccount) {
        this.transactionFees = transactionFees;
        this.oldGASAccount = oldGASAccount;
        this.newGASAccount = newGASAccount;
        this.bakerReward = bakerReward;
        this.foundationCharge = foundationCharge;
        this.baker = baker;
        this.foundationAccount = foundationAccount;
    }
}
