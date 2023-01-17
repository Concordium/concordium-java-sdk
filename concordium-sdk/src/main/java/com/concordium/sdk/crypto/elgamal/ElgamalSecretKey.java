package com.concordium.sdk.crypto.elgamal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ElgamalSecretKey {

    /**
     * The bytes of the secret key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static ElgamalSecretKey from(String hexKey) {
        try {
            return new ElgamalSecretKey(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ElgamalSecretKey", e);
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
