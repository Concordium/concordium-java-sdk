package com.concordium.sdk.responses.rewardstatus;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Data;

/**
 * Information about total amount of CCD and the state of various administrative accounts.
 */
@Data
@Builder
public class RewardsOverview {

    /**
     * The amount in the baking reward account.
     */
    private final CCDAmount bakingRewardAccount;

    /**
     * The amount in the finalization reward account.
     */
    private final CCDAmount finalizationRewardAccount;

    /**
     * The transaction reward fraction accruing to the foundation (to be paid at next payday).
     */
    private final CCDAmount foundationTransactionRewards;

    /**
     * The amount in the GAS account.
     */
    private final CCDAmount gasAccount;

    /**
     * he rate at which CCD will be minted (as a proportion of the total supply) at the next payday
     */
    private final double nextPaydayMintRate;

    /**
     * The time of the next payday.
     */
    private final Timestamp nextPaydayTime;

    /**
     * Protocol version that applies to these rewards. V0 variant only exists for protocol versions 1, 2, and 3.
     */
    private final ProtocolVersion protocolVersion;

    /**
     * The total CCD in existence.
     */
    private final CCDAmount totalAmount;

    /**
     * The total CCD in encrypted balances.
     */
    private final CCDAmount totalEncryptedAmount;

    /**
     * The total capital put up as stake by bakers and delegators
     */
    private final CCDAmount totalStakedCapital;
}
