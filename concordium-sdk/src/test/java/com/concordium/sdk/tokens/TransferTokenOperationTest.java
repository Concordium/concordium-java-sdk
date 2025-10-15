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
        val expectedHex = "bf687472616e73666572bf66616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7646d656d6fd81848674d79206d656d6fffff";
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
        val operationAExpectedHex = "bf687472616e73666572bf66616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7ffff";
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
        val operationBExpectedHex = "bf687472616e73666572bf66616d6f756e74c48223187b69726563697069656e74d99d73a10358201515151515151515151515151515151515151515151515151515151515151515ffff";
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

    @Test
    @SneakyThrows
    public void testTokenTransferOperationListSerialization() {

        val operations = new TokenOperation[]{
                TransferTokenOperation
                        .builder()
                        .amount(new TokenOperationAmount(UInt64.from("12"), 1))
                        .recipient(new TaggedTokenHolderAccount(
                                AccountAddress.from(
                                        "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                                )
                        ))
                        .build(),
                TransferTokenOperation
                        .builder()
                        .amount(new TokenOperationAmount(UInt64.from(88800000), 6))
                        .recipient(
                                new TaggedTokenHolderAccount(
                                        AccountAddress.from(
                                                new byte[]{94, 72, 117, -42, -7, -90, -5, -101, -30, -67, -62, -91, 54, 9, 74, 29, 42, 6, 27, -43, 80, -22, 36, -78, 54, 108, -100, 14, 63, -18, -23, 21}
                                        )
                                )
                        )
                        .memo(
                                CborMemo.from(
                                        Hex.decode("a2696368616c6c656e676578403738613466383166363031323535326164613636396361313163663430663961666431313665613435303161393239336266396233636131376233343132623667707572706f7365781c41726d656e69616e2057696e652043656c6c6172205061796d656e74")
                                )
                        )
                        .build()
        };
        val expectedHex = "82bf687472616e73666572bf66616d6f756e74c482200c69726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7ffffbf687472616e73666572bf66616d6f756e74c482251a054afb0069726563697069656e74d99d73a10358205e4875d6f9a6fb9be2bdc2a536094a1d2a061bd550ea24b2366c9c0e3feee915646d656d6fd8185873a2696368616c6c656e676578403738613466383166363031323535326164613636396361313163663430663961666431313665613435303161393239336266396233636131376233343132623667707572706f7365781c41726d656e69616e2057696e652043656c6c6172205061796d656e74ffff";

        Assert.assertEquals(
                expectedHex,
                Hex.toHexString(CborMapper.INSTANCE.writeValueAsBytes(operations))
        );
        Assert.assertArrayEquals(
                operations,
                CborMapper.INSTANCE
                        .readerForArrayOf(TokenOperation.class)
                        .readValue(Hex.decode(expectedHex))
        );
    }
}
