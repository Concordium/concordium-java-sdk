package com.concordium.sdk.types;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UInt {
    public static final int BYTES = 1;
    public static final int MAX_VALUE = 255;
    public static final int MIN_VALUE = 0;

    private final byte value;

    public static UInt from(final byte value) {
        return new UInt((byte) (value & 0xFF));
    }

    public static UInt from(final int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return new UInt((byte) value);
    }
}
