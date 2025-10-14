package com.concordium.sdk.transactions;

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
                "b61c9f2d8407ccfb849fc12d202c352e4bcb50fef9f6c6dd85115e379a0c75fe",
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
                "6db4f1828dc94e1d2c5b6ded5f6d7fdac79a44733abce9986624dbe643921858",
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
                UInt64.from("743"),
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
