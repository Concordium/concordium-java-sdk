package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.val;


final class ED25519 {

    static {
        NativeResolver.loadLib();
    }

    static final int KEY_SIZE = 32;

    static final int SIGNATURE_SIZE = 64;

    /**
     * Signs an input `message` using input {@link ED25519SecretKey} `secretKey`.
     *
     * @param secretKey {@link ED25519SecretKey} to sign the input `message` with.
     * @param message   Message to sign
     * @return Signature
     * @throws ED25519Exception When the Signing Fails.
     */
    static byte[] sign(ED25519SecretKey secretKey, byte[] message) {
        val buff = new byte[SIGNATURE_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.sign(secretKey.getBytes(), message, buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return buff;
    }

    /**
     * Verifies that input `signature` on input message
     * is signed using secret key of input {@link ED25519PublicKey} `publicKey`
     *
     * @param publicKey {@link ED25519PublicKey}.
     * @param message   Message which is signed.
     * @param signature Signature
     * @return `true` Or `false` depending on the verification.
     * @throws ED25519Exception If the Verification fails.
     */
    static boolean verify(ED25519PublicKey publicKey, byte[] message, byte[] signature) {
        val resultCode = ED25519ResultCode.from(CryptoJniNative.verify(publicKey.getBytes(), message, signature));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return true;
    }

    /**
     * Creates a new Instance of {@link ED25519SecretKey}.
     *
     * @return Instance of {@link ED25519SecretKey}.
     * @throws ED25519Exception If the creation fails.
     */
    static ED25519SecretKey makeSecretKey() {
        val buff = new byte[KEY_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.generateSecretKey(buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return ED25519SecretKey.from(buff);
    }

    /**
     * Creates a new public Key from the input {@link ED25519SecretKey} secret key.
     *
     * @param secretKey {@link ED25519SecretKey}.
     * @return {@link ED25519PublicKey}.
     * @throws ED25519Exception If the Creation fails.
     */
    static ED25519PublicKey makePublicKey(ED25519SecretKey secretKey) {
        val secretKeyBytes = secretKey.getBytes();
        val buff = new byte[KEY_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.generatePublicKey(secretKeyBytes, buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return ED25519PublicKey.from(buff);
    }
}
