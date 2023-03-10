package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import java.lang.NullPointerException;

import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt16;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UpdateCredentialKeysTransactionTest {
    final static int[] EXPECTED_UPDATE_CREDENTIAL_KEYS_TRANSACTION_BYTES = new int[]{0, 0, 1, 0, 2, 0, 0, 64, 204, 17, 80, 222, 17, 46, 151, 77, 70, 101, 116, 91, 51, 207, 4, 54, 96, 228, 125, 176, 217, 15, 246, 244, 119, 228, 215, 209, 44, 141, 117, 69, 100, 158, 45, 218, 148, 57, 29, 205, 242, 70, 27, 203, 132, 166, 228, 29, 91, 244, 250, 41, 199, 212, 193, 16, 75, 225, 84, 75, 129, 50, 57, 8, 1, 0, 64, 95, 72, 242, 28, 83, 2, 150, 93, 184, 246, 56, 143, 83, 95, 86, 154, 16, 51, 95, 106, 196, 35, 71, 246, 197, 148, 204, 99, 208, 241, 159, 125, 153, 199, 49, 192, 11, 229, 36, 118, 220, 210, 132, 153, 108, 231, 94, 241, 168, 120, 113, 41, 240, 76, 227, 97, 140, 251, 214, 223, 34, 1, 215, 6, 157, 35, 6, 113, 171, 110, 250, 242, 134, 31, 11, 89, 66, 230, 80, 24, 96, 54, 184, 251, 180, 233, 151, 63, 86, 52, 180, 62, 102, 77, 59, 75, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 11, 129, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};

    @Test
    @SneakyThrows
    public void testUpdateCredentialKeys() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        val transaction = UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .signer(TransactionTestHelper.getValidSigner())
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();

        assertEquals("920903215efd15190ff183746cb3380037fc851d203c3ac97876664d2c3743dd", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_UPDATE_CREDENTIAL_KEYS_TRANSACTION_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getVersionedBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutSenderFails() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutNonceFails() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .expiry(Expiry.from(1669466666))
                .signer(TransactionTestHelper.getValidSigner())
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutExpiryFails() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .signer(TransactionTestHelper.getValidSigner())
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();

        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutSignerShouldFail() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    public void testCreateUpdateCredentialKeysTransactionWithInvalidSignerFails() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .signer(getSignerWithMalformedSecretKey())
                .numExistingCredentials(UInt16.from(5))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
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
