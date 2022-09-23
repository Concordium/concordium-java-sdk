package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.fail;

public class InitContractTransactionTest {
    byte[] emptyArray = new byte[0];

    Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");

    @Test
    public void testInitContract() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .maxEnergyCost(UInt64.from(3000))
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                    .build();
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }

    }

    @Test
    public void testCreateInitContractTransactionWithoutSenderFails() {
        try {
            InitContractTransaction
                    .builder()
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                    .build();
            fail("Expected InitContractTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Sender cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }


    @Test
    public void testCreateInitContractTransactionWithoutAmountFails() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .build();
            fail("Expected InitContractTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Amount cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateInitContractTransactionWithoutNonceFails() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionTestHelper.getValidSigner())
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                    .build();
            fail("Expected InitContractTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. AccountNonce cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateInitContractTransactionWithoutExpiryFails() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .signer(TransactionTestHelper.getValidSigner())
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                    .build();
            fail("Expected InitContractTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Expiry cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateInitContractTransactionWithoutSignerShouldFail() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
                    .build();
            fail("Expected InitContractTransaction to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Signer cannot be null or empty")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateInitContractTransactionWithInvalidSignerFails() {
        try {
            InitContractTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getSignerWithMalformedSecretKey())
                    .maxEnergyCost(UInt64.from(3000))
                    .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", emptyArray))
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
