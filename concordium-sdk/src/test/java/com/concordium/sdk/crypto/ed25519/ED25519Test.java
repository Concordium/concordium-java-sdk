package com.concordium.sdk.crypto.ed25519;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.exceptions.ED25519Exception;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ED25519Test {

    static {
        ED25519.loadNatives();
    }

    @Test
    public void testSignVerify() {
        try {
            val message = SHA256.hash("Hello, World!".getBytes(StandardCharsets.UTF_8));
            val secretKey = ED25519SecretKey.createNew();
            val publicKey = ED25519PublicKey.from(secretKey);
            val keypair = ED25519Keypair.from(secretKey, publicKey);
            val signature = keypair.sign(message);
            assertEquals(64, signature.length);
            assertTrue(keypair.verify(message, signature));
            val secretKeyBytes = secretKey.getBytes();
            val secretKeyHex = Hex.encodeHexString(secretKeyBytes);
            val publicKeyBytes = publicKey.getBytes();
            val publicKeyHex = Hex.encodeHexString(publicKeyBytes);
            assertEquals(secretKey, ED25519SecretKey.from(secretKeyBytes));
            assertEquals(secretKey, ED25519SecretKey.from(secretKeyHex));
            assertEquals(publicKey, ED25519PublicKey.from(publicKeyBytes));
            assertEquals(publicKey, ED25519PublicKey.from(publicKeyHex));
        } catch (ED25519Exception e) {
            fail(e.getCode().getErrorMessage());
        }

    }

    // test vectors taken from: https://datatracker.ietf.org/doc/html/rfc8032#page-24
    @Test
    public void checkTestVectors() {
        try {
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
        } catch (ED25519Exception | DecoderException e) {
            fail("Unexpected error: " + e.getMessage());
        }

    }
}
