package com.concordium.sdk.cis2.events;

import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.Hash;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted from a CIS2 compliant contract with some metadata.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Cis2EventWithMetadata {

    /**
     * The event
     */
    private final Cis2Event event;

    /**
     * The block of which the event was emitted in.
     */
    private final BlockQuery blockIdentifier;

    /**
     * The hash of the transaction that caused the event.
     */
    private final Hash origin;

    public Cis2EventWithMetadata(Cis2Event event, BlockQuery blockIdentifier, Hash origin) {
        this.event = event;
        this.blockIdentifier = blockIdentifier;
        this.origin = origin;
    }
}
