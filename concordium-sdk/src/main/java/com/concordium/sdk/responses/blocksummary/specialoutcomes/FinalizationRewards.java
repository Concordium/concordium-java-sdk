package com.concordium.sdk.responses.blocksummary.specialoutcomes;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Payment to each finalizer on inclusion of a finalization
 * record in a block.
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
public final class FinalizationRewards extends SpecialOutcome {

    /**
     * The amount awarded to each finalizer.
     */
    private final Map<AccountAddress, CCDAmount> finalizationRewards;
    /**
     * The remaining balance of the finalization reward account.
     */
    private final CCDAmount remainder;


    @JsonCreator
    FinalizationRewards(
            @JsonProperty("finalizationRewards") Map<AccountAddress, CCDAmount> finalizationRewards,
            @JsonProperty("remainder") CCDAmount remainder) {
        this.finalizationRewards = finalizationRewards;
        this.remainder = remainder;
    }
}
