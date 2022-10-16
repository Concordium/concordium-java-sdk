package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class InitContractTransactionTest {
    byte[] emptyArray = new byte[0];

    Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
    final static int[] EXPECTED_INIT_CONTRACT_TRANSACTION_BYTES = new int[] {0, 0, 1, 0, 1, 0, 0, 64, 61, 179, 210, 23, 215, 251, 74, 202, 11, 22, 219, 251, 41, 73, 105, 218, 158, 233, 203, 63, 77, 234, 214, 34, 237, 239, 102, 155, 141, 125, 90, 65, 225, 219, 158, 27, 151, 227, 70, 233, 13, 181, 251, 244, 102, 134, 180, 222, 142, 132, 105, 181, 127, 19, 245, 39, 85, 3, 116, 28, 56, 80, 203, 5, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 146, 0, 0, 0, 58, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 13, 105, 110, 105, 116, 95, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};

    @Test
    @SneakyThrows
    public void testInitContract() {
        val transaction = InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                .build();
        assertEquals("fe69d75d93b63bdc3651b5b420b6a1b2950b7e7c8c4336bc15db9f00c210fd80", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_INIT_CONTRACT_TRANSACTION_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithoutSenderFails() {
        InitContractTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                .maxEnergyCost(UInt64.from(3000))
                .build();
        fail("Expected InitContractTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithoutAmountFails() {
        InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .build();
        fail("Expected InitContractTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithoutNonceFails() {
        InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                .maxEnergyCost(UInt64.from(3000))
                .build();
        fail("Expected InitContractTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithoutExpiryFails() {
        InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                .maxEnergyCost(UInt64.from(3000))
                .build();
        fail("Expected InitContractTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithoutSignerShouldFail() {
        InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                .maxEnergyCost(UInt64.from(3000))
                .build();
        fail("Expected InitContractTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateInitContractTransactionWithInvalidSignerFails() {
        InitContractTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .maxEnergyCost(UInt64.from(3000))
                .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
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
