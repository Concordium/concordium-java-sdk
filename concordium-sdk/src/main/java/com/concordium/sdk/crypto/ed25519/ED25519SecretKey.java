package com.concordium.sdk.crypto.ed25519;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

@Getter
public final class ED25519SecretKey {
    private final byte[] bytes;

    private ED25519SecretKey(byte[] bytes) {
        this.bytes = bytes;
    }

    @SneakyThrows
    public static ED25519SecretKey from(String hexKey) {
        return new ED25519SecretKey(Hex.decodeHex(hexKey));
    }

    public static ED25519SecretKey from(byte[] bytes) {
        return new ED25519SecretKey(bytes);
    }

    public byte[] sign(byte[] message) {
        return ED25519.sign(this, message);
    }

    public static ED25519SecretKey make() {
        return ED25519.makeSecretKey();
    }
}
