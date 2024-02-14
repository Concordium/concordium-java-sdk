package com.concordium.sdk.cis2.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A custom event emitted from a CIS2 compliant contract.
 */
@EqualsAndHashCode
@ToString
@Getter
public class CustomEvent implements Cis2Event {

    private final byte tag;
    private final byte[] data;

    public CustomEvent(byte tag, byte[] data) {
        this.tag = tag;
        this.data = data;
    }

    @Override
    public Type getType() {
        return Type.CUSTOM;
    }
}
