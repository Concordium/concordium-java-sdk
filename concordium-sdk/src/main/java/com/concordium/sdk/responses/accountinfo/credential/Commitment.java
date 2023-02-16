package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.DecoderException;
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

    @JsonCreator
    public static Commitment from(final String hex) {
        try {
            return new Commitment(Hex.decodeHex(hex));
        } catch (DecoderException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Commitment from(final byte[] bytes) {
        return new Commitment(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(value);
    }
}
