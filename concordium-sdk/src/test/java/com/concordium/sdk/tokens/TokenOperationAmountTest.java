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
        val amount = new TokenOperationAmount(
                UInt64.from("1500000"),
                6
        );
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(amount);
        Assert.assertEquals(
                "c482251b000000000016e360",
                Hex.toHexString(encoded)
        );
    }
}
