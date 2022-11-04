package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.responses.transactionstatus.ContractAddress;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import org.junit.Test;


import static org.junit.Assert.*;

public class UpdateContractTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 1, 0, 0, 64, 171, 120, 141, 90, 114, 116, 192, 90, 63, 4, 240, 13, 44, 227, 153, 49, 71, 86, 147, 112, 214, 228, 77, 243, 254, 248, 79, 251, 206, 64, 87, 106, 54, 80, 165, 61, 136, 245, 3, 11, 107, 206, 209, 56, 98, 211, 217, 125, 48, 78, 115, 130, 226, 189, 52, 47, 112, 226, 110, 44, 61, 12, 187, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 39, 218, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
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
        assertEquals("727d4b0413d0306c607d45d8265ad9c21271ab52147492a882a151ef11b7dfa8", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
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
    @SneakyThrows
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
