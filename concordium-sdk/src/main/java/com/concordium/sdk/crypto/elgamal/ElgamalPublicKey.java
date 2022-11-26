package com.concordium.sdk.crypto.elgamal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ElgamalPublicKey {

    /**
     * The bytes of the public key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static ElgamalPublicKey from(String hexKey) {
        try {
            return new ElgamalPublicKey(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ElgamalPublicKey", e);
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
