package com.concordium.sdk.responses;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An 'Epoch'.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Epoch {
    private final UInt64 value;

    public Epoch(UInt64 value) {
        this.value = value;
    }

    public static Epoch from(UInt64 x) {
        return new Epoch(x);
    }

    public static Epoch from(long x) {
        return new Epoch(UInt64.from(x));
    }
}
