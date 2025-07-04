package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
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
        val op = TransferTokenOperation
                .builder()
                .amount(new TokenOperationAmount(UInt64.from("1500000"), 6))
                .memo(new CborMemo("My memo"))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(op);
        Assert.assertEquals(
                "a1687472616e73666572a301c482251b000000000016e36002d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b703d818674d79206d656d6f",
                Hex.toHexString(encoded)
        );
    }

    @Test
    @SneakyThrows
    public void testTokenTransferOperationWithoutMemoSerialization() {
        val op = TransferTokenOperation
                .builder()
                .amount(new TokenOperationAmount(new BigDecimal("1.5"), 6))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(op);
        Assert.assertEquals(
                "a1687472616e73666572a301c482251b000000000016e36002d99d73a103582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b703f6",
                Hex.toHexString(encoded)
        );
    }
}
