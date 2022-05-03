package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final List<PendingUpdate<T>> pendingUpdates;

    @JsonCreator
    EnqueuedUpdate(@JsonProperty("nextSequenceNumber") Nonce nextSequenceNumber,
                   @JsonProperty("queue") List<PendingUpdate<T>> pendingUpdates) {
        this.nextSequenceNumber = nextSequenceNumber;
        // pending updates is either a type or a tuple consisting e i.e. in json terms a list containing two elements.
        // the first element is a unix timestamp of the effectiveTime.
        // The second element is the actual update.
        this.pendingUpdates = pendingUpdates;
    }
}
