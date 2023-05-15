package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents an amount of energy.
 */
@Getter
@EqualsAndHashCode
@ToString
public class Energy {

    private final UInt64 value;

    private Energy(UInt64 value) {
        this.value = value;
    }

    public static Energy from(UInt64 value) {
        return new Energy(value);
    }

    public static Energy from(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value of Energy cannot be negative");
        }
        return new Energy(UInt64.from(value));
    }
}
