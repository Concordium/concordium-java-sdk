package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.val;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransferWithMemoTransactionTest {

    @Test
    public void testCreateTransferWithMemo() {
        try {
            val transaction = TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("6b48656c6c6f20576f726c64"))
                    .sender(AccountAddress.from("3a3gm7Bd2xDvRN6eMfHDXWXHyxuDaq3CQGEAXiXRTMi7pV4rax"))
                    .receiver(AccountAddress.from("3YeRQYpKSvDRsH8hWY6KTJ6TJuYxM6MUEYW4wCVZ1je4UA8ar9"))
                    .amount(GTUAmount.fromMicro(1000000))
                    .nonce(AccountNonce.from(1))
                    .expiry(Expiry.from(1632131765))
                    .signer(TransactionSigner.from(
                            SignerEntry.from(Index.from(0), Index.from(0),
                                    ED25519SecretKey.from("b427f9ee55c966805dac37216864348452d42322a25b7ed8b7c56f7926b2e111")),
                            SignerEntry.from(Index.from(0), Index.from(1),
                                    ED25519SecretKey.from("69ab1478b950175720a199846e114d5a25de0ddca8539744be680cf3702e695a")),
                            SignerEntry.from(Index.from(0), Index.from(2),
                                    ED25519SecretKey.from("b8ae248d6a18bcd40187f255e9f0ef3e593190a371f7c0fa6ff6814c71b066f7"))))
                    .build();
            assertEquals("dfee5f4aede94657790cc314474c9caff76f836a99cef9ad6688c5644c36d8b0", transaction.getHash().asHex());
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void testCreateTransferWithMemoTwo() {
        try {
            val transaction = TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from(new byte[]{1, 2, 3, 4, 5}))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(TransactionSigner.from(
                            SignerEntry.from(Index.from(0), Index.from(0),
                                    ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                            SignerEntry.from(Index.from(0), Index.from(1),
                                    ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                    ))
                    .build();
            assertEquals("2cf00d7d5064ab6f70102a8bba4082b7d85b9b411f981f00b5994adc0b461083", transaction.getHash().asHex());
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void testCreateTransferWithMemoTransactionWithoutMemoFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Memo cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferWithMemoTransactionWithoutSenderFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Sender cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithoutReceiverFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Receiver cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithoutAmountFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Amount cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithoutNonceFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .expiry(Expiry.from(123456))
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. AccountNonce cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithoutExpiryFails() {
        try {
            AccountAddress sender = AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc");
            AccountAddress receiver = AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM");
            GTUAmount amount = GTUAmount.fromMicro(17);
            AccountNonce accountNonce = AccountNonce.from(78910);

            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(sender)
                    .receiver(receiver)
                    .amount(amount)
                    .nonce(accountNonce)
                    .signer(getValidSigner())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Expiry cannot be null")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithoutSignerShouldFail() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Signer cannot be null or empty")) {
                fail("Unexpected error: " + e);
            }
        }
    }

    @Test
    public void testCreateTransferTransactionWithInvalidSignerFails() {
        try {
            TransferWithMemoTransaction
                    .builder()
                    .memo(Memo.from("Hello, World!".getBytes(StandardCharsets.UTF_8)))
                    .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(GTUAmount.fromMicro(17))
                    .nonce(AccountNonce.from(78910))
                    .expiry(Expiry.from(123456))
                    .signer(getSignerWithMalformedSecretKey())
                    .build();
            fail("Expected TransferWithMemo to fail");
        } catch (TransactionCreationException e) {
            val inner = (ED25519Exception) e.getInner();
            if (!inner.getCode().equals(ED25519ResultCode.MALFORMED_SECRET_KEY))
                fail("Unexpected error: " + e.getMessage());
        }
    }


    private TransactionSigner getValidSigner() {
        ED25519SecretKey firstSecretKey = ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2");
        ED25519SecretKey secondSecretKey = ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9");
        return TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        firstSecretKey),
                SignerEntry.from(Index.from(0), Index.from(1),
                        secondSecretKey));
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
