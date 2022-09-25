package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferScheduleTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 160, 147, 116, 10, 241, 62, 244, 7, 148, 141, 21, 45, 26, 115, 36, 82, 92, 95, 181, 35, 170, 64, 115, 142, 133, 213, 255, 189, 36, 60, 202, 182, 175, 58, 146, 237, 20, 118, 137, 65, 104, 14, 162, 122, 13, 187, 61, 244, 156, 101, 78, 229, 71, 98, 173, 108, 195, 150, 50, 215, 235, 106, 45, 13, 1, 0, 64, 110, 143, 178, 218, 170, 78, 170, 246, 167, 27, 218, 74, 199, 177, 241, 38, 196, 193, 90, 164, 87, 21, 165, 186, 138, 201, 149, 226, 48, 134, 80, 16, 182, 203, 46, 180, 67, 64, 31, 223, 77, 228, 73, 131, 250, 114, 132, 14, 65, 161, 49, 148, 135, 60, 128, 105, 96, 12, 129, 22, 245, 70, 96, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 70, 0, 0, 0, 50, 0, 0, 0, 0, 0, 1, 226, 64, 19, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @Test
    @SneakyThrows
    public void testTransferScheduleTransaction() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        val transaction = TransferScheduleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .schedule(schedule)
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("d4088348be353a78fb01051d45894b0d85ae911d15f81cb55c9b7eb8cc6cfe38", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testTransferScheduleTransactionWithoutSenderFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleTransaction
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
    public void testTransferScheduleTransactionWithoutNonceFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleTransaction
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
    public void testTransferScheduleTransactionWithoutExpiryFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleTransaction
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
    public void testTransferScheduleTransactionWithoutSignerShouldFail() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleTransaction
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
    public void testTransferScheduleTransactionWithInvalidSignerFails() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        TransferScheduleTransaction
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
