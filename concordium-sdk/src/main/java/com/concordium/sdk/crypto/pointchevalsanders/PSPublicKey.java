package com.concordium.sdk.crypto.pointchevalsanders;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

/**
 * Public Key for Pointcheval-Sanders Signing scheme.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
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
}
