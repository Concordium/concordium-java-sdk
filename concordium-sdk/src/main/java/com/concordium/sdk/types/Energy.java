package com.concordium.sdk.types;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents an amount of energy.
 */
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

    /**
     * Parses {@link com.concordium.grpc.v2.Energy} to {@link Energy}.
     * @param energy {@link com.concordium.grpc.v2.Energy} returned by the GRPC V2 API.
     * @return parsed {@link Energy}.
     */
    public static Energy parse(com.concordium.grpc.v2.Energy energy) {
        return from(energy.getValue());
    }

    public long getValue() {
        return value.getValue();
    }
}
