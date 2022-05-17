package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.nio.ByteBuffer;

/**
 * A `Nonce`
 */
@Getter
@EqualsAndHashCode
@ToString
public class Nonce {
    /**
     * The underlying value
     */
    private final UInt64 value;

    private Nonce(UInt64 value) {
        this.value = value;
    }

    @JsonCreator
    Nonce(long nonce) {
        this.value = UInt64.from(nonce);
    }

    public static Nonce from(long value) {
        return new Nonce(UInt64.from(value));
    }

    public static Nonce fromBytes(ByteBuffer source) {
        return new Nonce(UInt64.fromBytes(source));
    }

    /**
     * Create a new nonce with an incremented underlying value.
     * @return the new `Nonce`.
     */
    public Nonce increment() {
        return new Nonce(UInt64.from(this.value.getValue() + 1));
    }

    public byte[] getBytes() {
        return this.value.getBytes();
    }
}
