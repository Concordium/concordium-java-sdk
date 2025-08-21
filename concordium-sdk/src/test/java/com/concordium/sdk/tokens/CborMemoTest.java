package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.CborMemo;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;

public class CborMemoTest {

    @SneakyThrows
    @Test
    public void testCborMemoStringSerialization() {
        Assert.assertEquals(
                "d8184c6b48656c6c6f20776f726c64",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                CborMemo.from("Hello world")
                        )
                )
        );
        Assert.assertEquals(
                "d818584f784dd09dd0b5d0bfd0bbd0bed185d0be20d181d180d0b0d0b1d0bed182d0b0d0bdd0be2c20d0bcd0b8d181d182d0b5d18020d0a0d0b0d0b72dd094d0b2d0b020f09f91a9f09f8fbbe2808df09f94ac",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                CborMemo.from("Неплохо сработано, мистер Раз-Два 👩🏻‍🔬")
                        )
                )
        );
    }

    @SneakyThrows
    @Test
    public void testCborMemoRawSerialization() {
        Assert.assertEquals(
                "d81843010203",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                CborMemo.from(new byte[]{1, 2, 3})
                        )
                )
        );
    }

    @SneakyThrows
    @Test
    public void testCborMemoNullSerialization() {
        Assert.assertEquals(
                "d81841f6",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                CborMemo.from((Object) null)
                        )
                )
        );
    }

    @SneakyThrows
    @Test(
            expected = IllegalArgumentException.class
    )
    public void testCborMemoTooBig() {
        CborMemo.from(SecureRandom.getSeed(257));
    }
}
