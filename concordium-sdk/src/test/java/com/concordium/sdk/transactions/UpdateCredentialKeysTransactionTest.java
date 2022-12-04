package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519ResultCode;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.responses.accountinfo.credential.Key;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UpdateCredentialKeysTransactionTest {
    final static int[] EXPECTED_UPDATE_CREDENTIAL_KEYS_TRANSACTION_BYTES = new int[] {0, 0, 1, 0, 2, 0, 0, 64, 111, 75, 158, 164, 72, 5, 185, 96, 211, 219, 86, 216, 60, 233, 237, 172, 31, 158, 130, 37, 87, 5, 243, 104, 189, 6, 47, 229, 116, 107, 247, 189, 233, 163, 100, 72, 45, 111, 43, 196, 207, 224, 61, 184, 77, 151, 135, 3, 148, 223, 203, 172, 180, 245, 150, 187, 49, 69, 36, 182, 5, 252, 44, 6, 1, 0, 64, 109, 188, 83, 34, 218, 178, 46, 158, 48, 251, 158, 22, 142, 153, 162, 84, 62, 216, 113, 66, 144, 29, 19, 186, 150, 193, 223, 19, 74, 26, 31, 166, 22, 27, 157, 49, 179, 221, 125, 68, 141, 77, 26, 172, 34, 219, 1, 228, 190, 46, 229, 124, 134, 8, 71, 202, 173, 68, 160, 207, 69, 43, 104, 8, 157, 35, 6, 113, 171, 110, 250, 242, 134, 31, 11, 89, 66, 230, 80, 24, 96, 54, 184, 251, 180, 233, 151, 63, 86, 52, 180, 62, 102, 77, 59, 75, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 13, 17, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};

    @Test
    @SneakyThrows
    public void testUpdateCredentialKeys() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        val transaction = UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();

        assertEquals("61d5835cbb169be1d8b0c59a34921b2ccb0ce43e9ca5de4663355579687292a6", transaction.getHash().asHex());
        assertArrayEquals(EXPECTED_UPDATE_CREDENTIAL_KEYS_TRANSACTION_BYTES, TestUtils.signedByteArrayToUnsigned(transaction.getBytes()));
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutSenderFails() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutNonceFails() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .expiry(Expiry.from(1669466666))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutExpiryFails() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .signer(TransactionTestHelper.getValidSigner())
                .maxEnergyCost(UInt64.from(3000))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();

        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithoutSignerShouldFail() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .maxEnergyCost(UInt64.from(3000))
                .keys(credentialPublicKeys)
                .credentialRegistrationID(regId)
                .build();
        fail("Expected UpdateCredentialKeysTransaction to fail");
    }

    @Test(expected = TransactionCreationException.class)
    @SneakyThrows
    public void testCreateUpdateCredentialKeysTransactionWithInvalidSignerFails() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        UpdateCredentialKeysTransaction
                .builder()
                .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
                .nonce(AccountNonce.from(525))
                .expiry(Expiry.from(1669466666))
                .signer(getSignerWithMalformedSecretKey())
                .maxEnergyCost(UInt64.from(3000))
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
