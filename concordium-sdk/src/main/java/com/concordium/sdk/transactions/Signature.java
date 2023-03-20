package com.concordium.sdk.transactions;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class Signature {
    private final byte[] bytes;

    @ToString.Exclude
    private final int length;

    private Signature(final byte[] bytes) {
        this.bytes = bytes;
        this.length = bytes.length;
    }

    @Builder
    public static Signature from(final byte[] sig) {
        return new Signature(sig);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }
}
