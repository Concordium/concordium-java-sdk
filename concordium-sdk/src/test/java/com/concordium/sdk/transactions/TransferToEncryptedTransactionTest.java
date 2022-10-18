package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferToEncryptedTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 70, 134, 69, 38, 96, 89, 66, 159, 210, 143, 4, 38, 107, 188, 183, 231, 208, 87, 85, 143, 16, 124, 185, 89, 194, 145, 252, 243, 227, 245, 253, 106, 176, 221, 6, 151, 241, 63, 89, 162, 131, 115, 114, 200, 85, 253, 200, 31, 28, 135, 20, 89, 8, 128, 192, 38, 0, 191, 114, 167, 8, 0, 61, 13, 1, 0, 64, 165, 27, 236, 148, 204, 45, 155, 232, 162, 164, 96, 64, 160, 6, 25, 28, 98, 1, 96, 220, 135, 41, 64, 253, 145, 193, 100, 236, 36, 65, 74, 205, 247, 30, 136, 202, 28, 52, 67, 103, 202, 176, 217, 157, 41, 255, 63, 224, 114, 186, 239, 56, 53, 61, 107, 117, 170, 233, 172, 50, 128, 219, 244, 11, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 29, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};

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
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("d8561cbb58b3ed6b316ae4377f2f38b70ac22300c7e2a08769bddb6cd0f3e031", transaction.getHash().asHex());
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
                .maxEnergyCost(UInt64.from(10000))
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
                .maxEnergyCost(UInt64.from(10000))
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
                .maxEnergyCost(UInt64.from(10000))
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
                .maxEnergyCost(UInt64.from(10000))
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
                .maxEnergyCost(UInt64.from(10000))
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
