package com.concordium.sdk.responses.accountinfo.credential;

import lombok.EqualsAndHashCode;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

import static java.util.Arrays.copyOf;

@EqualsAndHashCode
/**
 * A single commitment in the G1 group of the BLS curve. This is always 48 bytes in length.
 */
public class Commitment {
    private final byte[] value;

    Commitment(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return copyOf(value, value.length);
    }

    public static Commitment from(final byte[] bytes) {
        return new Commitment(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(value);
    }
}
