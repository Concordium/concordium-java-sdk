package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The effective time of a pending update associated with the actual update.
 * @param <T> the update.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class PendingUpdate<T> {

    private final Timestamp effectiveTime;
    private final T update;

    @JsonCreator
    PendingUpdate(@JsonProperty("effectiveTime") long effectiveTime, @JsonProperty("update") T update) {
        this.effectiveTime = Timestamp.newSeconds(effectiveTime);
        this.update = update;
    }
}
