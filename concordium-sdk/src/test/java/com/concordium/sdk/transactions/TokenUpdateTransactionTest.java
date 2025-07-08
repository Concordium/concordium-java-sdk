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
                "24303123d37c83a071bf861bb5d4490f945e4253234890062d851fd75c4bf25c",
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol(
                                                TokenId
                                                        .newBuilder()
                                                        .setValue("TEST")
                                                        .build()
                                        )
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
                "76d7e2c3f3e2046eb145783f7bda8c26c216e2f2db3a195f41ffb7640981077a",
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol(
                                                com.concordium.grpc.v2.plt.TokenId
                                                        .newBuilder()
                                                        .setValue("TEST")
                                                        .build()
                                        )
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
                UInt64.from("751"),
                TransactionFactory
                        .newTokenUpdate()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .payload(
                                TokenUpdate
                                        .builder()
                                        .tokenSymbol(
                                                com.concordium.grpc.v2.plt.TokenId
                                                        .newBuilder()
                                                        .setValue("TEST")
                                                        .build()
                                        )
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
