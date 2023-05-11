package com.concordium.sdk.crypto.pointchevalsanders;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Public Key for Pointcheval-Sanders Signing scheme.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
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

    public static PSPublicKey from(byte[] bytes) {
        return new PSPublicKey(bytes);
    }
}
