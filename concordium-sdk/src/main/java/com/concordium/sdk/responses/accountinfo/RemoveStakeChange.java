package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * The baker has removed its stake.
 */
public class RemoveStakeChange extends PendingChange {

    @JsonCreator
    public RemoveStakeChange(@JsonProperty("effectiveTime") OffsetDateTime effectiveTime) {
        super(effectiveTime);
    }
}
