package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * The baker has removed its stake.
 */
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class RemoveStakeChange extends PendingChange {

    @JsonCreator
    public RemoveStakeChange(@JsonProperty("effectiveTime") Timestamp effectiveTime) {
        super(effectiveTime);
    }
}
