package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Payment to each baker of a previous epoch,
 * in proportion to the number of blocks they
 * contributed.
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
public final class BakingRewards extends SpecialOutcome {
    /**
     * The amount awarded to each baker.
     */
    private final List<Reward> bakerRewards;

    /**
     * The remaining balance of the baker reward account.
     */
    private final CCDAmount remainder;
}
