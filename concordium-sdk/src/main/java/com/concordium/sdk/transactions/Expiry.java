package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Getter;

@Getter
public class Expiry {
    private final UInt64 value;

    private Expiry(UInt64 value) {
        this.value = value;
    }

    public static Expiry from(long value) {
        if (value == 0) {
            throw new IllegalArgumentException("Expiry cannot be zero");
        }
        return new Expiry(UInt64.from(value));
    }
}
