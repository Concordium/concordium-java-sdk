package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import java.lang.NullPointerException;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.Timestamp;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferScheduleTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 246, 216, 156, 14, 50, 8, 14, 87, 170, 5, 30, 90, 144, 78, 81, 250, 45, 188, 29, 68, 217, 66, 12, 235, 209, 19, 96, 6, 251, 120, 22, 0, 113, 143, 248, 233, 152, 80, 238, 161, 183, 42, 189, 43, 200, 145, 190, 224, 236, 155, 67, 228, 86, 235, 106, 219, 48, 212, 243, 121, 164, 180, 55, 7, 1, 0, 64, 211, 217, 179, 23, 238, 86, 44, 111, 240, 86, 133, 127, 40, 249, 31, 52, 201, 196, 36, 239, 70, 178, 84, 126, 236, 77, 90, 51, 143, 37, 1, 194, 110, 200, 72, 255, 4, 208, 82, 142, 198, 13, 17, 162, 8, 244, 142, 186, 151, 190, 56, 206, 170, 194, 160, 59, 137, 28, 7, 58, 64, 125, 160, 6, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 2, 162, 0, 0, 0, 50, 0, 0, 0, 0, 0, 1, 226, 64, 19, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @Test
    @SneakyThrows
    public void testTransferScheduleTransaction() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        val transaction = TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .build();
        assertEquals("cd4a0cef4600d3f228eb91874f080e928abbd488e1d7bb16cd96ccdfafd2fee8", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getVersionedBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithoutSenderFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        TransferScheduleTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithoutNonceFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .build();
        fail("Expected TransferTransaction to fail");

    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithoutExpiryFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .build();
        fail("Expected DeployTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithoutSignerShouldFail() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithInvalidSignerFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
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
