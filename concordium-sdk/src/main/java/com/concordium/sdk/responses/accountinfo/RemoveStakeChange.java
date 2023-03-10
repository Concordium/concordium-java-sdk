package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

/**
 * The baker has removed its stake.
 */
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoveStakeChange extends PendingChange {

    @JsonCreator
    @Builder
    public RemoveStakeChange(@JsonProperty("effectiveTime") OffsetDateTime effectiveTime) {
        super(effectiveTime);
    }
}
