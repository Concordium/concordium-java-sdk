package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * The baker has reduced its stake.
 */
@ToString
@Getter
public class ReduceStakeChange extends PendingChange {

    /**
     * The new stake for the baker.
     */
    private final CCDAmount newStake;

    @JsonCreator
    public ReduceStakeChange(@JsonProperty("effectiveTime") OffsetDateTime effectiveTime,
                             @JsonProperty("newStake") CCDAmount newStake) {
        super(effectiveTime);
        this.newStake = newStake;
    }
}
