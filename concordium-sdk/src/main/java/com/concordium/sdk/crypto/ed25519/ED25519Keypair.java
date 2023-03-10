package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.NonNull;

public final class ED25519Keypair {
    private final ED25519SecretKey secretKey;
    private final ED25519PublicKey publicKey;

    private ED25519Keypair(@NonNull final ED25519SecretKey secretKey, @NonNull final ED25519PublicKey publicKey) {
        this.secretKey = secretKey;
        this.publicKey = publicKey;
    }

    /**
     * Signs input `message`
     *
     * @param message Message to sign.
     * @return Signature on the input message
     * @throws ED25519Exception If the Signining fails.
     */
    public byte[] sign(byte[] message) {
        return secretKey.sign(message);
    }

    /**
     * Verifies signature on the input message.
     *
     * @param message   Message to verify the signature on.
     * @param signature Signature
     * @return `true` or `false` representing verification success or failure.
     * @throws ED25519Exception If the Verification fails.
     */
    public boolean verify(byte[] message, byte[] signature) {
        return publicKey.verify(message, signature);
    }

    public static ED25519Keypair from(final ED25519SecretKey privateKey, final ED25519PublicKey publicKey) {
        try {
            return new ED25519Keypair(privateKey, publicKey);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
