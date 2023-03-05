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
    final static int[] EXPECTED_INIT_CONTRACT_TRANSACTION_BYTES = new int[] {0, 0, 1, 0, 2, 0, 0, 64, 208, 15, 41, 133, 150, 59, 194, 128, 109, 109, 213, 49, 112, 78, 234, 29, 74, 11, 212, 39, 122, 99, 181, 37, 30, 146, 43, 84, 196, 18, 251, 106, 14, 101, 76, 3, 182, 113, 52, 13, 216, 168, 3, 192, 235, 108, 214, 117, 37, 27, 170, 222, 219, 63, 54, 167, 161, 214, 190, 79, 147, 88, 58, 4, 1, 0, 64, 47, 217, 145, 51, 79, 34, 0, 180, 95, 144, 58, 60, 227, 236, 139, 91, 18, 191, 17, 23, 47, 38, 118, 173, 217, 149, 132, 90, 230, 3, 139, 42, 139, 195, 254, 237, 188, 136, 188, 207, 64, 23, 61, 238, 230, 14, 114, 128, 11, 102, 155, 33, 217, 28, 35, 33, 157, 128, 74, 61, 204, 124, 6, 0, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 246, 0, 0, 0, 58, 0, 0, 0, 0, 0, 1, 226, 64, 1, 0, 0, 0, 0, 0, 0, 0, 0, 55, 238, 179, 233, 32, 37, 201, 126, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7, 0, 13, 105, 110, 105, 116, 95, 67, 73, 83, 50, 45, 78, 70, 84, 0, 0};

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
        assertEquals("111becf05b32822cb6a10bb6dfe8bee35a03f199320acb37a6bb05a804ac0a19", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_INIT_CONTRACT_TRANSACTION_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = NullPointerException.class)
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


    @Test(expected = NullPointerException.class)
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

    @Test(expected = NullPointerException.class)
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

    @Test(expected = NullPointerException.class)
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

    @Test(expected = NullPointerException.class)
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
