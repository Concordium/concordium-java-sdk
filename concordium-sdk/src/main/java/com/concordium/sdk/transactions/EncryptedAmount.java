package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import static java.util.Arrays.copyOf;

@EqualsAndHashCode
public class EncryptedAmount {

    /**
     * The bytes of the encrypted amount.
     */
    private final byte[] bytes;

    public static EncryptedAmount from(com.concordium.grpc.v2.EncryptedAmount inputAmount) {
        return new EncryptedAmount(inputAmount.getValue().toByteArray());
    }

    public byte[] getBytes() {
        return copyOf(bytes, bytes.length);
    }

    private EncryptedAmount(final byte[] bytes) {
        this.bytes = bytes;
    }

    @JsonCreator
    public static EncryptedAmount from(String hexKey) {
        try {
            return new EncryptedAmount(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create EncryptedAmount", e);
        }
    }

    public static EncryptedAmount from(byte[] bytes) {
        return new EncryptedAmount(copyOf(bytes, bytes.length));
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }
}
