package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeployModuleTransactionTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 164, 1, 86, 25, 45, 188, 246, 12, 182, 134, 202, 198, 236, 215, 100, 45, 127, 140, 61, 36, 62, 48, 86, 254, 81, 83, 56, 13, 208, 191, 71, 200, 5, 207, 97, 114, 193, 169, 212, 180, 114, 172, 44, 234, 103, 211, 118, 118, 244, 60, 112, 205, 19, 101, 90, 124, 143, 115, 234, 106, 106, 59, 196, 9, 1, 0, 64, 59, 242, 76, 160, 93, 213, 229, 120, 121, 83, 23, 53, 31, 82, 201, 4, 34, 220, 70, 88, 188, 98, 100, 160, 201, 157, 216, 164, 4, 191, 152, 164, 238, 85, 188, 189, 146, 98, 240, 249, 209, 93, 97, 183, 142, 54, 127, 227, 183, 43, 230, 106, 9, 48, 130, 204, 1, 122, 206, 2, 249, 230, 2, 9, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 40, 53, 0, 0, 0, 33, 0, 0, 0, 0, 0, 1, 226, 64, 0, 0, 0, 0, 1, 0, 0, 0, 24, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7};

    byte[] mod_ref = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07").getBytes();

    @Test
    @SneakyThrows
    public void testDeployModuleTransaction() {
        val transaction = DeployModuleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .module(WasmModule.from(mod_ref, 1))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        assertEquals("4e9ee036e1bacd81eeeb69bbe0e310d65da4af25fbdd3502d2e91a42fd93c7fe", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_TRANSACTION_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testDeployModuleTransactionWithoutSenderFails() {
        DeployModuleTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .module(WasmModule.from(mod_ref, 1))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testDeployModuleTransactionWithoutNonceFails() {
        DeployModuleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .module(WasmModule.from(mod_ref, 1))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected TransferTransaction to fail");

    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testDeployModuleTransactionWithoutExpiryFails() {
        DeployModuleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .signer(TransactionTestHelper.getValidSigner())
                .module(WasmModule.from(mod_ref, 1))
                .maxEnergyCost(UInt64.from(10000))
                .build();
        fail("Expected DeployTransaction to fail");
    }


    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testDeployModuleTransactionWithoutSignerShouldFail() {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .module(WasmModule.from(mod_ref, 1))
                    .maxEnergyCost(UInt64.from(10000))
                    .build();
            fail("Expected TransferTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testDeployModuleTransactionWithInvalidSignerFails() {
        DeployModuleTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(getSignerWithMalformedSecretKey())
                .module(WasmModule.from(mod_ref, 1))
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
