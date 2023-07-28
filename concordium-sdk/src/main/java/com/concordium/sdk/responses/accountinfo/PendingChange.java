package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A pending change for a baker.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@SuperBuilder
public abstract class PendingChange {

    /**
     * The effective time.
     */
    @JsonProperty("effectiveTime")
    private final Timestamp effectiveTime;
}
