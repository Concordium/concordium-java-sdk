package com.concordium.sdk.crypto.elgamal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ElgamalPublicKey {

    /**
     * The bytes of the public key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static ElgamalPublicKey from(String hexKey) {
        try {
            return from(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ElgamalPublicKey", e);
        }
    }

    /**
     * Creates an Elgamal Public Key from {@link byte} Array.
     *
     * @param bytes Input Byte Array
     * @return Instance of {@link ElgamalPublicKey}
     */
    public static ElgamalPublicKey from(final byte[] bytes) {
        return new ElgamalPublicKey(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
