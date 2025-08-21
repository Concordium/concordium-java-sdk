package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class TokenOperationAmountTest {

    @Test
    @SneakyThrows
    public void testTokenOperationAmountSerialization() {
        Assert.assertEquals(
                "c482251b000000000016e360",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TokenOperationAmount(
                                        UInt64.from("1500000"),
                                        6
                                )
                        )
                )
        );
        Assert.assertEquals(
                "c482221b000000000012d687",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TokenOperationAmount(
                                        UInt64.from("1234567"),
                                        3
                                )
                        )
                )
        );
        Assert.assertEquals(
                "c482221b8000000000000000",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TokenOperationAmount(
                                        UInt64.from(Long.MIN_VALUE),
                                        3
                                )
                        )
                )
        );
        Assert.assertEquals(
                "c482221b7fffffffffffffff",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TokenOperationAmount(
                                        UInt64.from(Long.MAX_VALUE),
                                        3
                                )
                        )
                )
        );
        Assert.assertEquals(
                "c482221bffffffffffffffff",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                new TokenOperationAmount(
                                        UInt64.from(-1),
                                        3
                                )
                        )
                )
        );
    }
}
