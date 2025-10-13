package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class TokenOperationAmountTest {

    @Test
    @SneakyThrows
    public void testTokenOperationAmountSerialization() {
        val amountA = new TokenOperationAmount(
                UInt64.from("1500000"),
                6
        );
        val amountAExpectedHex = "c482251a0016e360";
        Assert.assertEquals(
                amountAExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(amountA))
        );
        Assert.assertEquals(
                amountA,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(amountAExpectedHex),
                        TokenOperationAmount.class
                )
        );
        val amountB = new TokenOperationAmount(
                UInt64.from("1234567"),
                3
        );
        val amountBExpectedHex = "c482221a0012d687";
        Assert.assertEquals(
                amountBExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(amountB))
        );
        Assert.assertEquals(
                amountB,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(amountBExpectedHex),
                        TokenOperationAmount.class
                )
        );
        val amountC =  new TokenOperationAmount(
                UInt64.from(Long.MIN_VALUE),
                3
        );
        val amountCExpectedHex = "c482221b8000000000000000";
        Assert.assertEquals(
                amountCExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(amountC))
        );
        Assert.assertEquals(
                amountC,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(amountCExpectedHex),
                        TokenOperationAmount.class
                )
        );
        val amountD =  new TokenOperationAmount(
                UInt64.from(Long.MAX_VALUE),
                3
        );
        val amountDExpectedHex = "c482221b7fffffffffffffff";
        Assert.assertEquals(
                amountDExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(amountD))
        );
        Assert.assertEquals(
                amountD,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(amountDExpectedHex),
                        TokenOperationAmount.class
                )
        );
        val amountE = new TokenOperationAmount(
                UInt64.from(-1),
                3
        );
        val amountEExpectedHex = "c482221bffffffffffffffff";
        Assert.assertEquals(
                amountEExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(amountE))
        );
        Assert.assertEquals(
                amountE,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(amountEExpectedHex),
                        TokenOperationAmount.class
                )
        );
    }
}
