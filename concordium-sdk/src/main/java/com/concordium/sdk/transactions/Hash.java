package com.concordium.sdk.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@EqualsAndHashCode
public class Hash {
    @Getter
    private final byte[] bytes;

    @JsonCreator
    Hash(String encoded) {
        try {
            this.bytes = Hex.decodeHex(encoded);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create Hash", e);
        }
    }

    private Hash(byte[] hash) {
        this.bytes = hash;
    }

    public String asHex() {
        return Hex.encodeHexString(this.bytes);
    }

    public static Hash from(String hexHash) {
        return new Hash(hexHash);
    }

    public static Hash from(byte[] hash) {
        return new Hash(hash);
    }

    @Override
    public String toString() {
        return this.asHex();
    }

}
