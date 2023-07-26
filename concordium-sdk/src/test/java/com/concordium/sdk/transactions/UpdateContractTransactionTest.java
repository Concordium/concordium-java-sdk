package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateContractTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 250, 95, 135, 36, 130, 4, 204, 124, 57, 135, 200, 157, 44, 145, 227, 25, 103, 20, 130, 217, 85, 57, 216, 195, 159, 224, 29, 108, 49, 21, 62, 95, 216, 149, 119, 142, 95, 201, 64, 40, 121, 210, 64, 250, 59, 162, 224, 150, 162, 113, 203, 64, 220, 151, 244, 125, 143, 75, 167, 75, 156, 103, 215, 9, 1, 0, 64, 57, 165, 169, 169, 201, 134, 38, 158, 134, 199, 166, 227, 213, 235, 194, 173, 4, 213, 239, 171, 126, 167, 62, 228, 206, 228, 216, 99, 179, 73, 13, 210, 204, 155, 4, 203, 223, 123, 179, 190, 5, 227, 96, 54, 197, 89, 8, 185, 69, 206, 177, 102, 194, 249, 96, 243, 169, 41, 218, 94, 70, 74, 151, 12, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 62, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
    byte[] emptyArray = new byte[0];


    @Test
    @SneakyThrows
    public void testUpdateContractTransaction() {
        UpdateContractTransaction transaction = UpdateContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("ac20e2a4be17d4e85bfbbbdc430b249517cfc634eaa57baa7df6cd42d711d94c", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getVersionedBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testUpdateContractTransactionWithoutSenderFails() {
        UpdateContractTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testUpdateContractTransactionWithoutNonceFails() {
        UpdateContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testUpdateContractTransactionWithoutExpiryFails() {
        UpdateContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected DeployTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testUpdateContractTransactionWithoutSignerShouldFail() {
        UpdateContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    public void testUpdateContractTransactionWithInvalidSignerFails() {
        UpdateContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .payload(UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
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
