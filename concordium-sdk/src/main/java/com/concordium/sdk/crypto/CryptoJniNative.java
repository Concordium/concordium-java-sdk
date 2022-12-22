package com.concordium.sdk.crypto;

public class CryptoJniNative {

    public static native int sign(byte[] privateKey, byte[] message, byte[] out);

    public static native int verify(byte[] publicKey, byte[] message, byte[] signature);

    public static native int generateSecretKey(byte[] buffer);

    public static native int generatePublicKey(byte[] secretKey, byte[] buffer);

    public static native String createSecToPubTransfer(String input);

    public static native String generateEncryptedTransfer(String input);

}
