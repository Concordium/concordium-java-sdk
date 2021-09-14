package com.concordium.sdk.crypto.ed25519;

final class ED25519 {

    static byte[] sign(ED25519SecretKey secretKey, byte[] message){
        return sign(secretKey.getBytes(), message);
    }
    static boolean verify(ED25519PublicKey publicKey, byte[] message, byte[] signature) {
        return verify(publicKey.getBytes(), message, signature);
    }

    static ED25519SecretKey makeSecretKey() {
        byte[] secretKeyBytes = generateSecretKey();
        return ED25519SecretKey.from(secretKeyBytes);
    }

    static ED25519PublicKey makePublicKey(ED25519SecretKey secretKey) {
        byte[] secretKeyBytes = secretKey.getBytes();
        return ED25519PublicKey.from(generatePublicKey(secretKeyBytes));
    }

    private static native byte[] sign(byte[] privateKey, byte[] message);
    private static native boolean verify(byte[] publicKey, byte[] message, byte[] signature);
    private static native byte[] generateSecretKey();
    private static native byte[] generatePublicKey(byte[] secretKey);
}
