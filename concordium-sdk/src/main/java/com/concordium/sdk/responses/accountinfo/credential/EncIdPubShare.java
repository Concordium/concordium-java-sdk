package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

import static java.util.Arrays.copyOf;

/**
 * Share of the encryption of IdCredPub.
 */
@EqualsAndHashCode
public class EncIdPubShare {
    private final byte[] value;

    EncIdPubShare(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return copyOf(value, value.length);
    }

    @JsonCreator
    public static EncIdPubShare from(final String hex) {
        try {
            return new EncIdPubShare(Hex.decodeHex(hex));
        } catch (DecoderException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static EncIdPubShare from(final byte[] bytes) {
        return new EncIdPubShare(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(value);
    }
}
