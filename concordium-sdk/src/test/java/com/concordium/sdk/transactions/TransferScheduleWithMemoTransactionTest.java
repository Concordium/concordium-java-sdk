package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferScheduleWithMemoTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 210, 240, 8, 86, 234, 249, 114, 69, 127, 163, 71, 65, 53, 232, 35, 87, 40, 254, 81, 15, 135, 205, 137, 77, 195, 147, 219, 174, 72, 30, 232, 111, 124, 13, 235, 229, 240, 25, 132, 67, 232, 161, 1, 57, 4, 255, 242, 134, 132, 251, 123, 27, 137, 220, 40, 29, 171, 61, 149, 24, 170, 197, 134, 15, 1, 0, 64, 170, 6, 143, 159, 241, 70, 205, 149, 109, 179, 89, 135, 91, 119, 208, 129, 202, 160, 224, 86, 87, 239, 153, 2, 182, 189, 254, 232, 69, 178, 163, 19, 36, 89, 168, 100, 57, 89, 31, 183, 134, 199, 169, 169, 19, 88, 147, 147, 32, 218, 59, 96, 173, 164, 246, 112, 47, 156, 161, 161, 24, 222, 75, 9, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 77, 0, 0, 0, 57, 0, 0, 0, 0, 0, 1, 226, 64, 24, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 0, 5, 1, 2, 3, 4, 5, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @Test
    @SneakyThrows
    public void testTransferScheduleWithMemoTransaction() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        val transaction = TransferScheduleWithMemoTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .memo(Memo.from(new byte[]{1, 2, 3, 4, 5}))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("a5ca526705bc34f8bf9c15476002becebff113a3265ab3813a4188356273da82", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleWithMemoTransactionWithoutSenderFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleWithMemoTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleWithMemoTransactionWithoutNonceFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleWithMemoTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");

    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleWithMemoTransactionWithoutExpiryFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleWithMemoTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected DeployTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleWithMemoTransactionWithoutSignerShouldFail() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleWithMemoTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleWithMemoTransactionWithInvalidSignerFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleWithMemoTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
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
