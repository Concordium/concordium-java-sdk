package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The {@link Index} is used when setting up the {@link TransactionSigner} when signing a {@link Transaction}.
 * That is, the {@link Index} serves both as `CredentialIndex` and `KeyIndex`.
 */
@EqualsAndHashCode
@ToString
public class Index {
    @Getter
    private final byte value;

    private static final int BYTE_MAX_VALUE = 255;

    private Index(byte value) {
        this.value = value;
    }

    public static Index from(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("KeyIndex cannot be negative");
        }
        if (index > BYTE_MAX_VALUE) {
            throw new IllegalArgumentException("KeyIndex cannot exceed one byte (" + BYTE_MAX_VALUE + ") was " + index);
        }
        return new Index((byte) index);
    }
}
