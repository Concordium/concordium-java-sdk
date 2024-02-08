package com.concordium.sdk.cis2;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TokenAmountTest {
    @Test
    public void testCreateTokenWithMinValue() {
        TokenAmount.from("0");
    }

    @Test
    public void testCreateTokenWithMaxValue() {
        TokenAmount.from("115792089237316195423570985008687907853269984665640564039457584007913129639935");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenWithNegativeValue() {
        TokenAmount.from("-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenWithMoreThanMaxValue() {
        TokenAmount.from("115792089237316195423570985008687907853269984665640564039457584007913129639936");
    }

    @Test
    public void testSerializeDeserializeMinimumTokenAmount() {
        val tokenAmount = TokenAmount.from(0);
        val buffer = ByteBuffer.wrap(tokenAmount.encode());
        assertEquals(tokenAmount, TokenAmount.decode(buffer));
        assertFalse(buffer.hasRemaining());
    }

    @Test
    public void testSerializeDeserializeMaximumTokenAmount() {
        val tokenAmount = TokenAmount.from("115792089237316195423570985008687907853269984665640564039457584007913129639935");
        val buffer = ByteBuffer.wrap(tokenAmount.encode());
        assertEquals(tokenAmount, TokenAmount.decode(buffer));
        assertFalse(buffer.hasRemaining());
    }

    @SneakyThrows
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeTokenAmountThatExceeds37Bytes() {
        TokenAmount.decode(ByteBuffer.wrap(Hex.decodeHex("8080808080808080808080d2e38b94e9f4d3ca9bc3f0b19393ad8db7ab8ff3efc2e2ef969a01")));
    }

    @SneakyThrows
    @Test(expected = IllegalArgumentException.class)
    public void testSeserializeTokenAmountThatExceeds37Bytes() {
        val invalidTokenAmount = TokenAmount.from(0);
        Field amountField = TokenAmount.class.getDeclaredField("amount");
        amountField.setAccessible(true);
        amountField.set(invalidTokenAmount, new BigInteger("1115792089237316195423570985008687907853269984665640564039457584007913129639936"));
        invalidTokenAmount.encode();
    }
}
