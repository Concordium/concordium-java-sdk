package com.concordium.sdk.responses.accountinfo.credential;

import lombok.EqualsAndHashCode;
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

    public static EncIdPubShare from(final byte[] bytes) {
        return new EncIdPubShare(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(value);
    }
}
