package com.concordium.sdk.crypto.bulletproof;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BulletproofGenerators {
    private final byte[] bytes;

    private BulletproofGenerators(final byte[] bytes) {
        this.bytes = bytes;
    }

    public static BulletproofGenerators from(final byte[] bytes) {
        return new BulletproofGenerators(bytes);
    }

    @JsonCreator
    public static BulletproofGenerators from(final String hex) {
        try {
            return new BulletproofGenerators(Hex.decodeHex(hex));
        } catch (DecoderException e) {
            throw new RuntimeException(
                    String.format("Could not construct Bulletproof Generators from HEX encoded input"));
        }
    }

    @JsonValue
    public String toHex() {
        return Hex.encodeHexString(this.bytes);
    }

    @Override
    public String toString() {
        return this.toHex();
    }
}
