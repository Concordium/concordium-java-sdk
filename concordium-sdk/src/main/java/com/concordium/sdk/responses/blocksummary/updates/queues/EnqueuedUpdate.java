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
                   @JsonProperty("queue") List<List<Object>> pendingUpdates) {
        this.nextSequenceNumber = nextSequenceNumber;
        this.pendingUpdates = new ArrayList<>();
        for (List<Object> pendingUpdate : pendingUpdates) {
            if (pendingUpdate.size() != 2) {
                throw new IllegalArgumentException("Unexected tuple length. Expected 2 but was " + pendingUpdate.size());
            }
            Long effectiveTime = (Long) pendingUpdate.get(0);
            T update = (T) pendingUpdate.get(1);
            this.pendingUpdates.add(new Wrapper<>(Timestamp.newSeconds(effectiveTime), update));
        }
    }
}
