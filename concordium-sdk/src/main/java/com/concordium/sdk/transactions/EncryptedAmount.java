package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
public class EncryptedAmount {

    /**
     * The bytes of the encrypted amount.
     */
    private final byte[] bytes;

    private EncryptedAmount(final byte[] bytes) {
        this.bytes = bytes;
    }

    @JsonCreator
    public static EncryptedAmount from(String hexKey) {
        try {
            return new EncryptedAmount(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create AccountEncryptedAmount", e);
        }
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
