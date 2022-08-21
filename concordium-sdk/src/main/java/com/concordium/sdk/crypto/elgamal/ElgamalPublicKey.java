package com.concordium.sdk.crypto.elgamal;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@RequiredArgsConstructor
@Getter
public class ElgamalPublicKey {

    /**
     * Bytes for the public key.
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
}
