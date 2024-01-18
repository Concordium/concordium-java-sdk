package com.concordium.sdk.crypto.pointchevalsanders;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

/**
 * Public Key for Pointcheval-Sanders Signing scheme.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PSPublicKey {

    /**
     * Bytes for the public key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static PSPublicKey from(String hexKey) {
        try {
            return new PSPublicKey(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create PSPublicKey", e);
        }
    }

    public static PSPublicKey from(final byte[] bytes) {
        return new PSPublicKey(Arrays.copyOf(bytes, bytes.length));
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
