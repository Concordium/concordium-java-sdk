package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * A pending change for a baker.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class PendingChange {

    /**
     * The effective time.
     */
    @JsonProperty("effectiveTime")
    private final OffsetDateTime effectiveTime;
}
