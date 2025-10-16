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
                "9f743fdb00d9697cde98dc3e75621b40535ec84b953e83f8a4116cb2c61519c0",
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
                "3945fe655328c62cc75be7e06cc24c91336b0bd4854d930ed017757c42a3ffc2",
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
                UInt64.from("745"),
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

    @Test
    @SneakyThrows
    public void testTokenUpdateTransferTransactionCostWithMemo() {
        Assert.assertEquals(
                UInt64.from("828"),
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
                                                                CborMemo.from("Memo for the test, long memo, quite a long text for a memo if you ask me")
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
