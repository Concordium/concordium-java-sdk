package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Payment to each finalizer on inclusion of a finalization
 * record in a block.
 */
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@Getter
public final class FinalizationRewards extends SpecialOutcome {

    /**
     * The amount awarded to each finalizer.
     */
    private final List<Reward> rewards;
    /**
     * The remaining balance of the finalization reward account.
     */
    private final CCDAmount remainder;
}
