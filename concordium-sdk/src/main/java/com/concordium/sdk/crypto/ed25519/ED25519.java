package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.val;


final class ED25519 {

    static {
        loadNatives();
    }

    public static void loadNatives() {
        NativeResolver.loadLib();
    }

    static final int KEY_SIZE = 32;

    static final int SIGNATURE_SIZE = 64;

    static byte[] sign(ED25519SecretKey secretKey, byte[] message) throws ED25519Exception {
        val buff = new byte[SIGNATURE_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.sign(secretKey.getBytes(), message, buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return buff;
    }

    static boolean verify(ED25519PublicKey publicKey, byte[] message, byte[] signature) throws ED25519Exception {
        val resultCode = ED25519ResultCode.from(CryptoJniNative.verify(publicKey.getBytes(), message, signature));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return true;
    }

    static ED25519SecretKey makeSecretKey() throws ED25519Exception {
        val buff = new byte[KEY_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.generateSecretKey(buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return ED25519SecretKey.from(buff);
    }

    static ED25519PublicKey makePublicKey(ED25519SecretKey secretKey) throws ED25519Exception {
        val secretKeyBytes = secretKey.getBytes();
        val buff = new byte[KEY_SIZE];
        val resultCode = ED25519ResultCode.from(CryptoJniNative.generatePublicKey(secretKeyBytes, buff));
        if (resultCode.failed()) {
            throw ED25519Exception.from(resultCode);
        }
        return ED25519PublicKey.from(buff);
    }
}
