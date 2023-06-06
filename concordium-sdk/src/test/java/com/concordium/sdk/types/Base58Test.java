package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public class Base58Test {

    @Test
    public void testBase58EncodeDecode() {
        val data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        val encoded = Base58.encode(data);
        val decoded = Base58.decode(encoded);
        assertArrayEquals(data, decoded);
    }

    @Test
    public void testBase58EncodeCheckedDecodeChecked() {
        val data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        val encoded = Base58.encodeChecked(1, data);
        val decoded = Base58.decodeChecked(1, encoded);
        assertArrayEquals(data, decoded);
    }
}
