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

public class TransferTokenOperationTest {

    @Test
    @SneakyThrows
    public void testTokenTransferOperationSerialization() {
        val operation = TransferTokenOperation
                .builder()
                .amount(new TokenOperationAmount(UInt64.from("1500000"), 6))
                .memo(CborMemo.from("My memo"))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val expectedHex = "a1687472616e73666572a366616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7646d656d6fd81848674d79206d656d6f";
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

    @Test
    @SneakyThrows
    public void testTokenTransferOperationWithoutMemoSerialization() {
        val operationA = TransferTokenOperation
                .builder()
                .amount(new TokenOperationAmount(new BigDecimal("1.5"), 6))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val operationAExpectedHex = "a1687472616e73666572a266616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7";
        Assert.assertEquals(
                operationAExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operationA))
        );
        Assert.assertEquals(
                operationA,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(operationAExpectedHex),
                        TokenOperation.class
                )
        );

        val operationB = TransferTokenOperation
                .builder()
                .amount(new TokenOperationAmount(UInt64.from("123"), 4))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                Hex.decode("1515151515151515151515151515151515151515151515151515151515151515")
                        )
                ))
                .build();
        val operationBExpectedHex = "a1687472616e73666572a266616d6f756e74c48223187b69726563697069656e74d99d73a10358201515151515151515151515151515151515151515151515151515151515151515";
        Assert.assertEquals(
                operationBExpectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operationB))
        );
        Assert.assertEquals(
                operationB,
                CborMapper.INSTANCE.readValue(
                        Hex.decode(operationBExpectedHex),
                        TokenOperation.class
                )
        );
    }
}
