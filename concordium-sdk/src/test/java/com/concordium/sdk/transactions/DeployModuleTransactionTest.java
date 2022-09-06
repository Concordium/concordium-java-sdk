package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DeployModuleTransactionTest {

    byte[] mod_ref = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07").getBytes();

    @Test
    public void testDeployModuleTransaction() {
        try {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .module(WasmModule.from(mod_ref, 1))
                    .build();
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }

    }

    @Test
    public void testDeployModuleTransactionWithoutSenderFails() {
        try {
            DeployModuleTransaction
                    .builder()
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .module(WasmModule.from(mod_ref, 1))
                    .build();
            fail("Expected TransferTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Sender cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }


    @Test
    public void testDeployModuleTransactionWithoutNonceFails() {
        try {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .module(WasmModule.from(mod_ref, 1))
                    .build();
            fail("Expected TransferTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. AccountNonce cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }


    @Test
    public void testDeployModuleTransactionWithoutExpiryFails() {

        try {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .signer(TransactionTestHelper.getValidSigner())
                    .module(WasmModule.from(mod_ref, 1))
                    .build();
            fail("Expected DeployTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Expiry cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }

    }


    @Test
    public void testDeployModuleTransactionWithoutSignerShouldFail() {
        try {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .module(WasmModule.from(mod_ref, 1))
                    .build();
            fail("Expected TransferTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Signer cannot be null or empty")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testDeployModuleTransactionWithInvalidSignerFails() {
        try {
            DeployModuleTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getSignerWithMalformedSecretKey())
                    .module(WasmModule.from(mod_ref, 1))
                    .build();


        } catch (TransactionCreationException e) {
            val inner = (ED25519Exception) e.getInner();
            if (!inner.getCode().equals(ED25519ResultCode.MALFORMED_SECRET_KEY))
                fail("Unexpected error: " + e.getMessage());
        }
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
