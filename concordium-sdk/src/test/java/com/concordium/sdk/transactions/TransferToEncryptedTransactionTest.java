package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferToEncryptedTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 211, 2, 167, 82, 142, 176, 115, 94, 113, 95, 191, 154, 75, 170, 201, 213, 243, 148, 198, 153, 199, 92, 20, 215, 193, 37, 176, 213, 196, 146, 108, 58, 131, 14, 107, 60, 252, 126, 253, 177, 238, 119, 246, 219, 136, 3, 152, 113, 96, 21, 66, 195, 115, 129, 53, 57, 89, 209, 39, 120, 204, 84, 80, 0, 1, 0, 64, 153, 204, 108, 39, 70, 219, 155, 177, 31, 8, 67, 109, 30, 168, 25, 247, 160, 151, 114, 193, 132, 142, 153, 182, 131, 111, 81, 177, 250, 189, 98, 190, 179, 202, 139, 225, 233, 81, 147, 240, 7, 59, 48, 3, 231, 231, 228, 182, 159, 5, 169, 101, 123, 166, 154, 31, 44, 102, 109, 183, 27, 142, 114, 4, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 3, 101, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};

    @Test
    @SneakyThrows
    public void testTransferToEncryptedTransaction() {
        val transaction = TransferToEncryptedTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .amount(CCDAmount.fromMicro(1))
                .build();
        assertEquals("e777705bb7d8ecd020f1a261049b3f0020d4032026ba6fdbf311f2871a26688e", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferToEncryptedTransactionWithoutSenderFails() {
        TransferToEncryptedTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .amount(CCDAmount.fromMicro(1))
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferToEncryptedTransactionWithoutNonceFails() {
        TransferToEncryptedTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .amount(CCDAmount.fromMicro(1))
                .build();
        fail("Expected TransferTransaction to fail");

    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferToEncryptedTransactionWithoutExpiryFails() {
        TransferToEncryptedTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .amount(CCDAmount.fromMicro(1))
                .build();
        fail("Expected DeployTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferToEncryptedTransactionWithoutSignerShouldFail() {
        TransferToEncryptedTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .amount(CCDAmount.fromMicro(1))
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferToEncryptedTransactionWithInvalidSignerFails() {
        TransferToEncryptedTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .amount(CCDAmount.fromMicro(1))
                .build();
        fail("Expected TransferTransaction to fail");

    }


    private TransactionSigner getSignerWithMalformedSecretKey() {
        return new TransactionSigner() {
            @Override
            public TransactionSigner put(Index credentialIndex, Index keyIndex, Signer signer) {
                return this;
            }

            @Override
            public TransactionSignature sign(byte[] message) {
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
