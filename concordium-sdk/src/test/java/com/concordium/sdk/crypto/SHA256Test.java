package com.concordium.sdk.crypto;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.*;

// Test vectors taken from: https://www.di-mgt.com.au/sha_testvectors.html
public class SHA256Test {

    private final Map<String, String> testVectors = new HashMap<>();

    @Before
    @SneakyThrows
    public void setup(){
        testVectors.put("abc", "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad");
        testVectors.put("", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        testVectors.put("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
                "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1");
        testVectors.put("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu",
                "cf5b16a778af8380036ce59e7b0492370b249b11e8f07a51afac45037afee9d1");
    }

    @SneakyThrows
    @Test
    public void testSha256() {
        for (String k : testVectors.keySet()) {
            val message = k.getBytes(StandardCharsets.UTF_8);
            val expected = Hex.decodeHex(testVectors.get(k));
            assertArrayEquals(expected, SHA256.hash(message));
        }
    }

    @Test
    public void testSha256Properties() {
        val data = new byte[64];
        val expected = SHA256.hash(data);
        val shouldBeTheSame = SHA256.hash(data, 0, data.length);
        assertArrayEquals(expected, shouldBeTheSame);
    }

    @Test
    public void testHashTwiceProperty() {
        val data = new byte[64];
        val expected = SHA256.hash(SHA256.hash(data));
        val actual = SHA256.hashTwice(data);
        assertArrayEquals(expected, actual);

        val another = SHA256.hashTwice(data, 0, data.length);
        assertArrayEquals(actual, another);
    }
}
