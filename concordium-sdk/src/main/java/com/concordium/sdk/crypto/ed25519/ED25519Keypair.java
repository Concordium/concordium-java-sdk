package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;

import java.util.Objects;

public final class ED25519Keypair {
    private final ED25519SecretKey secretKey;
    private final ED25519PublicKey publicKey;

    private ED25519Keypair(ED25519SecretKey secretKey, ED25519PublicKey publicKey) {
        this.secretKey = secretKey;
        this.publicKey = publicKey;
    }

    public byte[] sign(byte[] message) throws ED25519Exception {
        return secretKey.sign(message);
    }

    public boolean verify(byte[] message, byte[] signature) throws ED25519Exception {
        return publicKey.verify(message, signature);
    }

    public static ED25519Keypair from(ED25519SecretKey privateKey, ED25519PublicKey publicKey) {
        if (Objects.isNull(privateKey)) {
            throw new IllegalArgumentException("Private key must not be null");
        }
        if (Objects.isNull(publicKey)) {
            throw new IllegalArgumentException("Public key must not be null");
        }
        return new ED25519Keypair(privateKey, publicKey);
    }

}
