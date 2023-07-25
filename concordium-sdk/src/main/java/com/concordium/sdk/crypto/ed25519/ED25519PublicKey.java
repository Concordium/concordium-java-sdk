package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
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
    public static ED25519PublicKey from(@NonNull String hexKey) {
        try {
            return from(Hex.decodeHex(hexKey));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Cannot create ED25519PublicKey", e);
        }
    }

    /**
     * Parses an input byte array into {@link ED25519PublicKey}.
     *
     * @param bytes bytes of {@link ED25519PublicKey}
     * @return Parsed {@link ED25519PublicKey}
     * @throws ED25519Exception When the length of the input byte array is not equal to {@link ED25519#KEY_SIZE}
     */
    public static ED25519PublicKey from(byte[] bytes) {
        if (bytes.length != ED25519.KEY_SIZE) {
            throw ED25519Exception.from(ED25519ResultCode.MALFORMED_PUBLIC_KEY);
        }

        return new ED25519PublicKey(copyOf(bytes, bytes.length));
    }

    /**
     * Verifies that the `signature` on the `message` has been created using private key of this {@link ED25519PublicKey}
     *
     * @param message   Message to verify the signature on.
     * @param signature Signature on the input `message`
     * @return `true` or `false` depending on if the verification was successful or not.
     * @throws ED25519Exception When the verification fails.
     */
    public boolean verify(byte[] message, byte[] signature) {
        return ED25519.verify(this, message, signature);
    }

    /**
     * Creates an instance of {@link ED25519PublicKey} from input {@link ED25519SecretKey}.
     *
     * @param secretKey Secret Key to generate the public key from.
     * @return Instantiated {@link ED25519PublicKey}
     * @throws ED25519Exception When the generation fails.
     */
    public static ED25519PublicKey from(ED25519SecretKey secretKey) {
        return ED25519.makePublicKey(secretKey);
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.getBytes());
    }
}
