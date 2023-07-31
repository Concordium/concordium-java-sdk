package com.concordium.sdk.crypto.bls;

import com.concordium.sdk.crypto.RawKey;
import com.concordium.sdk.crypto.KeyJsonSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@JsonSerialize(using = KeyJsonSerializer.class)
public class BLSPublicKey implements RawKey {
    /**
     * The bytes of the public key.
     */
    private final byte[] bytes;

    @JsonCreator
    public static BLSPublicKey from(String hexKey) {
        try {
            return from(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create BLSPublicKey", e);
        }
    }

    /**
     * Creates an BLS Public Key from {@link byte} Array.
     *
     * @param bytes Input Byte Array
     * @return Instance of {@link BLSPublicKey}
     */
    public static BLSPublicKey from(final byte[] bytes) {
        return new BLSPublicKey(Arrays.copyOf(bytes, bytes.length));
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.bytes);
    }

    @Override
    public byte[] getRawBytes() {
        return this.bytes;
    }
}
