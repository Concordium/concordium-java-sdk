package com.concordium.sdk.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
import com.concordium.sdk.types.AccountAddress;
import lombok.SneakyThrows;
import lombok.val;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransferTokenOperationTest {

    @Test
    @SneakyThrows
    public void testTokenTransferOperationSerialization() {
        val op = TransferTokenOperation
                .builder()
                .amount(new BigDecimal("1.5"))
                .memo(new CborMemo("My memo"))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(op);
        Assert.assertEquals(
                "bf66616d6f756e74c482200f69726563697069656e74d99d73bf03582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7ff646d656d6fd818674d79206d656d6fff",
                Hex.toHexString(encoded)
        );
    }

    @Test
    @SneakyThrows
    public void testTokenTransferOperationWithoutMemoSerialization() {
        val op = TransferTokenOperation
                .builder()
                .amount(new BigDecimal("1.5"))
                .recipient(new TaggedTokenHolderAccount(
                        AccountAddress.from(
                                "3CbvrNVpcHpL7tyT2mhXxQwNWHiPNYEJRgp3CMgEcMyXivms6B"
                        )
                ))
                .build();
        val encoded = CborMapper.INSTANCE.writeValueAsBytes(op);
//        Assert.assertEquals(
//                "bf66616d6f756e74c482200f69726563697069656e74d99d73bf03582021bc8745c81c07ca7f3fb79a8bd161624cb1d5da788baec13f5a5d9eac3a29b7ff646d656d6fd818674d79206d656d6fff",
//                Hex.toHexString(encoded)
//        );
    }
}
