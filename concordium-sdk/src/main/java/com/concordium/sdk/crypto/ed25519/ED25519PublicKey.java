package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import static java.util.Arrays.copyOf;

@Getter
@EqualsAndHashCode
public final class ED25519PublicKey {
    private final byte[] bytes;

    private ED25519PublicKey(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return copyOf(bytes, bytes.length);
    }

    @JsonCreator
    public static ED25519PublicKey from(String hexKey) throws ED25519Exception {
        try {
            return from(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ED25519PublicKey", e);
        }
    }

    public static ED25519PublicKey from(byte[] bytes) {
        if (bytes.length != ED25519.KEY_SIZE) {
            throw ED25519Exception.from(ED25519ResultCode.MALFORMED_SECRET_KEY);
        }

        return new ED25519PublicKey(copyOf(bytes, bytes.length));
    }

    public boolean verify(byte[] message, byte[] signature) throws ED25519Exception {
        return ED25519.verify(this, message, signature);
    }

    public static ED25519PublicKey from(ED25519SecretKey secretKey) throws ED25519Exception {
        return ED25519.makePublicKey(secretKey);
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.getBytes());
    }
}
