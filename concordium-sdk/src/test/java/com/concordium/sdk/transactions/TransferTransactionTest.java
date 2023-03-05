package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.val;
import org.junit.Test;

import static com.concordium.sdk.transactions.TestUtils.signedByteArrayToUnsigned;
import static org.junit.Assert.*;

public class TransferTransactionTest {

    public void testCreateTransfer() {
        val transfer = TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, signedByteArrayToUnsigned(transfer.getVersionedBytes()));
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", transfer.getHash().asHex());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutSenderFails() {
        TransferTransaction
                .builder()
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutReceiverFails() {
        TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutAmountFails() {
        TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutNonceFails() {
        TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutExpiryFails() {
        AccountAddress sender = AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc");
        AccountAddress receiver = AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM");
        CCDAmount amount = CCDAmount.fromMicro(17);
        AccountNonce accountNonce = AccountNonce.from(78910);

        TransferTransaction
                .builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nonce(accountNonce)
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTransferTransactionWithoutSignerShouldFail() {
        TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    public void testCreateTransferTransactionWithInvalidSignerFails() {
        TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .build();
    }


    private TransactionSigner getSignerWithMalformedSecretKey() {
        return new TransactionSigner() {
            @Override
            public TransactionSigner put(Index credentialIndex, Index keyIndex, Signer signer) {
                return this;
            }

            @Override
            public TransactionSignature sign(byte[] message) throws ED25519Exception {
                throw ED25519Exception.from(ED25519ResultCode.MALFORMED_SECRET_KEY);
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }
}
