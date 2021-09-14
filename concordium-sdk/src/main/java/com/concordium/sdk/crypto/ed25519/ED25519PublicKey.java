package com.concordium.sdk.crypto.ed25519;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

@Getter
public final class ED25519PublicKey {
    private final byte[] bytes;

    private ED25519PublicKey(byte[] bytes) {
        this.bytes = bytes;
    }

    @SneakyThrows
    public static ED25519PublicKey from(String hexKey) {
        return new ED25519PublicKey(Hex.decodeHex(hexKey));
    }

    public static ED25519PublicKey from(byte[] bytes) {
        return new ED25519PublicKey(bytes);
    }

    public boolean verify(byte[] message, byte[] signature) {
        return ED25519.verify(this, message, signature);
    }

    public static ED25519PublicKey from(ED25519SecretKey secretKey) {
        return ED25519.makePublicKey(secretKey);
    }
}
