package com.concordium.sdk.crypto;

import lombok.val;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    public static byte[] hash(byte[] contents) {
        return hash(contents, 0, contents.length);
    }

    public static byte[] hashTwice(byte[] contents) {
        val digest = getDigest();
        digest.update(contents, 0, contents.length);
        return digest.digest(digest.digest());
    }

    public static byte[] hash(byte[] contents, int offset, int length) {
        val digest = getDigest();
        digest.update(contents, offset, length);
        return digest.digest();
    }

    public static byte[] hashTwice(byte[] contents, int offset, int length) {
        val digest = getDigest();
        digest.update(contents, offset, length);
        return digest.digest(digest.digest());
    }

    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot get SHA256", e);
        }
    }

}
