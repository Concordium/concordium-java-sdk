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
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 90, 34, 88, 164, 164, 111, 244, 23, 15, 51, 120, 134, 89, 200, 11, 83, 18, 165, 244, 215, 181, 62, 37, 20, 123, 189, 75, 198, 132, 187, 228, 14, 254, 23, 62, 213, 61, 176, 181, 180, 152, 241, 79, 254, 135, 119, 150, 2, 237, 194, 251, 209, 150, 192, 200, 196, 51, 204, 246, 74, 160, 198, 70, 8, 1, 0, 64, 71, 216, 19, 15, 221, 113, 252, 12, 13, 227, 183, 223, 97, 60, 167, 16, 0, 19, 217, 148, 94, 126, 198, 13, 240, 90, 120, 64, 70, 149, 68, 37, 99, 3, 9, 2, 173, 205, 105, 247, 25, 136, 240, 158, 239, 161, 230, 58, 229, 189, 90, 4, 100, 114, 57, 47, 47, 250, 123, 247, 30, 161, 6, 3, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 62, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("55c4040b1540ac4fbe52d01bd1d4795b306ce65b71f879f62f5d5ef4df504785", transaction.getHash().asHex());
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
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
                .payload(UpdateContractPayload.from(0, ContractAddress.from(0, 81), "CIS2-NFT", "mint", emptyArray))
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
