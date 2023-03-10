package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

/**
 * The baker has reduced its stake.
 */
@ToString(callSuper = true)
@Getter
@Jacksonized
@EqualsAndHashCode(callSuper = true)
public class ReduceStakeChange extends PendingChange {

    /**
     * The new stake for the baker.
     */
    private final CCDAmount newStake;

    @JsonCreator
    @Builder
    public ReduceStakeChange(@JsonProperty("effectiveTime") OffsetDateTime effectiveTime,
                             @JsonProperty("newStake") CCDAmount newStake) {
        super(effectiveTime);
        this.newStake = newStake;
    }
}
