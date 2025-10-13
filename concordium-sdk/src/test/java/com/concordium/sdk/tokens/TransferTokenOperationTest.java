package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferTokenOperationTest {

    @Test
    @SneakyThrows
    public void testTokenTransferOperationSerialization() {
        Assert.assertEquals(
                "a1687472616e73666572bf66616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7646d656d6fd81848674d79206d656d6fff",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                TransferTokenOperation
                                        .builder()
                                        .amount(new TokenOperationAmount(UInt64.from("1500000"), 6))
                                        .memo(CborMemo.from("My memo"))
                                        .recipient(new TaggedTokenHolderAccount(
                                                AccountAddress.from(
                                                        "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                                                )
                                        ))
                                        .build()
                        )
                )
        );
    }

    @Test
    @SneakyThrows
    public void testTokenTransferOperationWithoutMemoSerialization() {
        Assert.assertEquals(
                "a1687472616e73666572bf66616d6f756e74c482251a0016e36069726563697069656e74d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7ff",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                TransferTokenOperation
                                        .builder()
                                        .amount(new TokenOperationAmount(new BigDecimal("1.5"), 6))
                                        .recipient(new TaggedTokenHolderAccount(
                                                AccountAddress.from(
                                                        "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                                                )
                                        ))
                                        .build()
                        )
                )
        );

        Assert.assertEquals(
                "a1687472616e73666572bf66616d6f756e74c48223187b69726563697069656e74d99d73a10358201515151515151515151515151515151515151515151515151515151515151515ff",
                Hex.toHexString(
                        CborMapper.INSTANCE.writeValueAsBytes(
                                TransferTokenOperation
                                        .builder()
                                        .amount(new TokenOperationAmount(UInt64.from("123"), 4))
                                        .recipient(new TaggedTokenHolderAccount(
                                                AccountAddress.from(
                                                        Hex.decode("1515151515151515151515151515151515151515151515151515151515151515")
                                                )
                                        ))
                                        .build()
                        )
                )
        );
    }
}
