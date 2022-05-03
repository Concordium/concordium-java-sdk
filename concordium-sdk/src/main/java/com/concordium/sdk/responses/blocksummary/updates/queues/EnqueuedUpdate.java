package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public final class EnqueuedUpdate<T> {
    /**
     * The next available sequence number for an update.
     */
    private final Nonce nextSequenceNumber;

    /**
     * Pending updates, in ascending order of effective time
     */
    private final List<Wrapper<T>> pendingUpdates;

    @JsonCreator
    EnqueuedUpdate(@JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                   @JsonProperty("queue") List<Wrapper<T>> pendingUpdates) {
        this.nextSequenceNumber = nextSequenceNumber;
        this.pendingUpdates = pendingUpdates;
    }
}
