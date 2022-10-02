package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@Getter
@EqualsAndHashCode
/**
 * Proof that an encrypted transfer data is well-formed
 */
public class SecToPubAmountTransferProof {

    /**
     * The bytes of the proof.
     */
    private final byte[] bytes;

    private SecToPubAmountTransferProof(final byte[] bytes) {
        this.bytes = bytes;
    }

    @JsonCreator
    public static SecToPubAmountTransferProof from(String hexKey) {
        try {
            return new SecToPubAmountTransferProof(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create SecToPubAmountTransferProof", e);
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
