package com.concordium.sdk.crypto;

import com.concordium.sdk.NativeResolver;
import com.concordium.sdk.crypto.ed25519.ED25519Keypair;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ED25519Test {

    static {
        NativeResolver.loadLib();
    }

    @Test
    public void testSignVerify() {
        val message = SHA256.hash("Hello, World!".getBytes(StandardCharsets.UTF_8));
        val secretKey = ED25519SecretKey.make();
        val publicKey = ED25519PublicKey.from(secretKey);
        val keypair  = ED25519Keypair.from(secretKey, publicKey);
        val signature = keypair.sign(message);
        assertEquals(64, signature.length);
        assertTrue(keypair.verify(message, signature));
        assertFalse(keypair.verify(message, new byte[64]));
    }

    // test vectors taken from: https://datatracker.ietf.org/doc/html/rfc8032#page-24
    @SneakyThrows
    @Test
    public void checkTestVectors() {
        val secretKey = ED25519SecretKey.from("c5aa8df43f9f837bedb7442f31dcb7b166d38535076f094b85ce3a2e0b4458f7");
        val message = "af82";
        val expectedSignature =
                "6291d657deec24024827e69c3abe01a3" +
                "0ce548a284743a445e3680d7db5ac3ac" +
                "18ff9b538d16f290ae67f760984dc659" +
                "4a7c15e9716ed28dc027beceea1ec40a";
        val signature = secretKey.sign(Hex.decodeHex(message));
        val publicKey = ED25519PublicKey.from(secretKey);
        assertTrue(publicKey.verify(Hex.decodeHex(message), signature));
        assertArrayEquals(Hex.decodeHex(expectedSignature), signature);
    }
}
