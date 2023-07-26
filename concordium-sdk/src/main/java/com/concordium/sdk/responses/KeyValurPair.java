package com.concordium.sdk.responses;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;

@Builder
@ToString
@EqualsAndHashCode
public class KeyValurPair {
    private final byte[] key;
    private final byte[] value;

    public byte[] getKey() {
        return Arrays.copyOf(key, key.length);
    }

    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
    }
}
