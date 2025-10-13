package com.concordium.sdk.transactions;

import com.concordium.grpc.v2.plt.TokenId;
import com.concordium.sdk.transactions.tokens.CborMemo;
import com.concordium.sdk.transactions.tokens.TaggedTokenHolderAccount;
import com.concordium.sdk.transactions.tokens.TokenOperationAmount;
import com.concordium.sdk.transactions.tokens.TransferTokenOperation;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TokenUpdateTransactionTest {

    @Test
    @SneakyThrows
    public void testTokenUpdateTransferTransactionWithTextMemo() {
        Assert.assertEquals(
                "556c31711e00fc73b7dc7a397bb863198d3059235052241059385bef0c3d0494",
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol("TEST")
                                        .operation(
                                                TransferTokenOperation
                                                        .builder()
                                                        .amount(
                                                                new TokenOperationAmount(
                                                                        new BigDecimal("1.5"),
                                                                        2
                                                                )
                                                        )
                                                        .recipient(
                                                                new TaggedTokenHolderAccount(
                                                                        AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM")
                                                                )
                                                        )
                                                        .memo(
                                                                CborMemo.from("Memo for the test")
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .nonce(Nonce.from(78910))
                        .expiry(Expiry.from(123456))
                        .signer(TransactionTestHelper.getValidSigner())
                        .build()
                        .getHash()
                        .asHex()
        );
    }

    @Test
    @SneakyThrows
    public void testTokenUpdateTransferTransactionWithoutMemo() {
        Assert.assertEquals(
                "cab78c06b978e35c1dab92c5aabb4d70a501ffa93ead5e0131a93c4d5ad7bc8d",
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol("TEST")
                                        .operation(
                                                TransferTokenOperation
                                                        .builder()
                                                        .amount(
                                                                new TokenOperationAmount(
                                                                        new BigDecimal("1.5"),
                                                                        2
                                                                )
                                                        )
                                                        .recipient(
                                                                new TaggedTokenHolderAccount(
                                                                        AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM")
                                                                )
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .nonce(Nonce.from(78910))
                        .expiry(Expiry.from(123456))
                        .signer(TransactionTestHelper.getValidSigner())
                        .build()
                        .getHash()
                        .asHex()
        );
    }

    @Test
    @SneakyThrows
    public void testTokenUpdateTransferTransactionCostWithoutMemo() {
        Assert.assertEquals(
                UInt64.from("744"),
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol("TEST")
                                        .operation(
                                                TransferTokenOperation
                                                        .builder()
                                                        .amount(
                                                                new TokenOperationAmount(
                                                                        new BigDecimal("1.5"),
                                                                        2
                                                                )
                                                        )
                                                        .recipient(
                                                                new TaggedTokenHolderAccount(
                                                                        AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM")
                                                                )
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .nonce(Nonce.from(78910))
                        .expiry(Expiry.from(123456))
                        .signer(TransactionTestHelper.getValidSigner())
                        .build()
                        .getHeader()
                        .getMaxEnergyCost()
        );
    }
}
