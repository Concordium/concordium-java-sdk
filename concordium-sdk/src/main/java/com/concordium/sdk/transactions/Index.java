package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * A non-negative index between 0 and 255.
 *
 * The {@link Index} is used when setting up the {@link TransactionSigner} when signing a {@link Transaction}.
 * That is, the {@link Index} serves both as `CredentialIndex` and `KeyIndex`.
 */
@EqualsAndHashCode
@ToString
public class Index implements Comparable {
    @Getter
    private final byte value;

    private static final int BYTE_MAX_VALUE = 255;

    private Index(byte value) {
        this.value = value;
    }

    @JsonCreator
    Index(String index) {
        val idx = Integer.parseUnsignedInt(index);
        if (idx > BYTE_MAX_VALUE) {
            throw new IllegalArgumentException("Index cannot exceed one byte (" + BYTE_MAX_VALUE + ") was " + index);
        }
        this.value = (byte) idx;
    }

    public static Index from(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }
        if (index > BYTE_MAX_VALUE) {
            throw new IllegalArgumentException("Index cannot exceed one byte (" + BYTE_MAX_VALUE + ") was " + index);
        }
        return new Index((byte) index);
    }

    public static Index fromBytes(ByteBuffer source) {
        return new Index(source.get());
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!(o instanceof Index)) {
            throw new ClassCastException();
        }
        Index other = (Index) o;
        if (this.value < other.value) {
            return -1;
        } else if (this.value == other.value) {
            return 0;
        } else {
            return 1;
        }
    }
}
