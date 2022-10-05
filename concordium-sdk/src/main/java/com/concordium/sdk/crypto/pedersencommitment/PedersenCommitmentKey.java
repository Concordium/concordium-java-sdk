package com.concordium.sdk.crypto.pedersencommitment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class PedersenCommitmentKey {
    private final byte[] bytes;

    private PedersenCommitmentKey(final byte[] bytes) {
        this.bytes = bytes;
    }

    public static PedersenCommitmentKey from(final byte[] bytes) {
        return new PedersenCommitmentKey(bytes);
    }

    @JsonCreator
    public static PedersenCommitmentKey from(final String hex) {
        try {
            return new PedersenCommitmentKey(Hex.decodeHex(hex));
        } catch (DecoderException e) {
            throw new RuntimeException(
                    String.format("Could not construct Commitment Key from HEX encoded input"));
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
