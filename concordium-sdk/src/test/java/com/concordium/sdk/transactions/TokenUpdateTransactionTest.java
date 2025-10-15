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
                "13a54dd42141e86fe155c5e8ead2a61b392316fce6019434e1e3ccc2ecb70cd3",
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
                "a4dc2c4bdb1e285ca90cc7fc1c8d2652f50acf0d5daf117f20302a3d8655ed5f",
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
                UInt64.from("734"),
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
