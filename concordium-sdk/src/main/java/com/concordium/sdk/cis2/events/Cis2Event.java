package com.concordium.sdk.cis2.events;

/**
 * A common API for an event emitted by a CIS2 compliant contract.
 */
public interface Cis2Event {
    /**
     * Get the type of the CIS2 event.
     */
    Type getType();


    /**
     * Types of events that are supported by the CIS2 specification
     */
    enum Type {
        TRANSFER,
        MINT,
        BURN,
        UPDATE_OPERATOR_OF,
        TOKEN_METADATA,
        CUSTOM;

        public static Type parse(byte tag) {
            if (tag == -1) return TRANSFER;
            if (tag == -2) return MINT;
            if (tag == -3) return BURN;
            if (tag == -4) return UPDATE_OPERATOR_OF;
            if (tag == -5) return TOKEN_METADATA;
            return CUSTOM;
        }
    }
}
