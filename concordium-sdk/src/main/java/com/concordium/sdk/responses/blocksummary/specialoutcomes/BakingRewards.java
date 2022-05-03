package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Payment to each baker of a previous epoch,
 * in proportion to the number of blocks they
 * contributed.
 */
@Getter
@ToString
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

    @JsonCreator
    BakingRewards(
            @JsonProperty("bakerRewards") List<Reward> bakerRewards,
            @JsonProperty("remainder") CCDAmount remainder) {
        this.bakerRewards = bakerRewards;
        this.remainder = remainder;
    }
}
