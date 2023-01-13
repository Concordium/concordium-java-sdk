package com.concordium.sdk.v2.types;

import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

public abstract class ByteArrayWrapper {
    private final byte[] bytes;

    ByteArrayWrapper(final byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
