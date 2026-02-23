package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.*;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class MintTokenOperationTest {

    @Test
    @SneakyThrows
    public void testMintTokenOperationSerialization() {
        val operation = MintTokenOperation
                .builder()
                .amount(new TokenOperationAmount(UInt64.from("15"), 6))
                .build();
        val expectedHex = "bf646d696e74bf66616d6f756e74c482250fffff";
        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operation))
        );
        Assert.assertEquals(
                operation,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(expectedHex),
                        TokenOperation.class
                )
        );
    }
}
