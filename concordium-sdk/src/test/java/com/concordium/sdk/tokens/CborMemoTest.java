package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.CborMemo;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;

public class CborMemoTest {

    @SneakyThrows
    @Test
    public void testCborMemoStringSerialization() {
        val cborMemo = new CborMemo("Interesting");
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(cborMemo);
        Assert.assertEquals("d8186b496e746572657374696e67", Hex.toHexString(encoded));
    }

    @SneakyThrows
    @Test
    public void testCborMemoRawSerialization() {
        val cborMemo = new CborMemo(new byte[]{1, 2, 3});
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(cborMemo);
        Assert.assertEquals("d81843010203", Hex.toHexString(encoded));
    }

    @SneakyThrows
    @Test(
            expected = IllegalArgumentException.class
    )
    public void testCborMemoTooBig() {
        new CborMemo(SecureRandom.getSeed(257));
    }
}
