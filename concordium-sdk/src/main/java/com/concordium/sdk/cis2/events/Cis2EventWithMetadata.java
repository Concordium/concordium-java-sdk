package com.concordium.sdk.cis2.events;

import com.concordium.sdk.cis2.Cis2Error;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.Hash;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * An event emitted from a CIS2 compliant contract with some metadata.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Cis2EventWithMetadata {

    /**
     * The event of a successfully executed transaction.
     * This is only present if the transaction was successfully executed.
     */
    private final Cis2Event event;

    /**
     * The error of a failed transaction.
     * This is only present if the tranasction failed.
     */
    private final Cis2Error error;

    /**
     * The block of which the event was emitted in.
     */
    private final BlockQuery blockIdentifier;

    /**
     * The hash of the transaction that caused the event.
     */
    private final Hash transactionHashOrigin;

    private Cis2EventWithMetadata(Cis2Event event, Cis2Error error, BlockQuery blockIdentifier, Hash origin) {
        this.event = event;
        this.error = error;
        this.blockIdentifier = blockIdentifier;
        this.transactionHashOrigin = origin;
    }

    /**
     * A flag that indicates whether the event retains an
     * {@link Cis2Event} (as a result of a successfully executed transaction)
     * or a {@link Cis2Error} as a result of a failed transaction.
     */
    public boolean isSuccessfull() {
        return Objects.isNull(error);
    }

    public static Cis2EventWithMetadata ok(Cis2Event event, BlockQuery blockIdentifier, Hash origin) {
        return new Cis2EventWithMetadata(event, null, blockIdentifier, origin);
    }

    public static Cis2EventWithMetadata err(Cis2Error error, BlockQuery blockIdentifier, Hash origin) {
        return new Cis2EventWithMetadata(null, error, blockIdentifier, origin);
    }
}
