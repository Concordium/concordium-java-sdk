package com.concordium.sdk.crypto;

import lombok.SneakyThrows;
import lombok.val;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    public static byte[] hash(byte[] contents) {
        return hash(contents,0, contents.length);
    }

    @SneakyThrows
    public static byte[] hashTwice(byte[] contents) {
        val digest = getDigest();
        digest.update(contents, 0, contents.length);
        return digest.digest(digest.digest());
    }

    @SneakyThrows
    public static byte[] hash(byte[] contents, int offset, int length) {
        val digest = getDigest();
        digest.update(contents, offset, length);
        return digest.digest();
    }

    @SneakyThrows
    public static byte[] hashTwice(byte[] contents, int offset, int length) {
        val digest = getDigest();
        digest.update(contents, offset, length);
        return digest.digest(digest.digest());
    }

    private static MessageDigest getDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

}
