package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * A pending change for a baker.
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class PendingChange {

    /**
     * The effective time.
     */
    private final OffsetDateTime effectiveTime;

    @JsonCreator
    public PendingChange(@JsonProperty("effectiveTime") OffsetDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
}
