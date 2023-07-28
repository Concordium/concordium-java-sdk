package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * The baker has reduced its stake.
 */
@ToString(callSuper = true)
@Getter
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ReduceStakeChange extends PendingChange {

    /**
     * The new stake for the baker.
     */
    private final CCDAmount newStake;

    @JsonCreator
    public ReduceStakeChange(@JsonProperty("effectiveTime") Timestamp effectiveTime,
                             @JsonProperty("newStake") CCDAmount newStake) {
        super(effectiveTime);
        this.newStake = newStake;
    }
}
