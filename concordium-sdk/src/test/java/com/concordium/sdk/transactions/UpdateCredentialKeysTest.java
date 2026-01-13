package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UpdateCredentialKeysTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_WITH_UPDATE_CREDENTIAL_KEYS_VERSIONED_BYTES = new int[]{0, 0, 1, 0, 2, 0, 0, 64, 165, 204, 162, 19, 99, 97, 140, 170, 232, 249, 53, 158, 10, 59, 38, 19, 113, 7, 74, 136, 193, 131, 104, 109, 224, 239, 155, 37, 89, 118, 108, 24, 52, 166, 201, 198, 124, 77, 217, 102, 50, 209, 218, 203, 131, 19, 36, 59, 136, 146, 242, 137, 40, 92, 238, 104, 29, 47, 171, 104, 168, 192, 200, 6, 1, 0, 64, 151, 113, 228, 228, 2, 117, 223, 77, 210, 86, 20, 142, 196, 243, 97, 103, 206, 194, 251, 211, 141, 208, 168, 74, 65, 213, 126, 85, 167, 246, 75, 16, 37, 178, 72, 246, 83, 10, 202, 44, 149, 25, 249, 34, 212, 100, 254, 19, 13, 193, 2, 148, 126, 191, 238, 133, 65, 172, 182, 66, 48, 9, 8, 4, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 11, 129, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};
    final static int[] EXPECTED_UPDATE_CREDENTIAL_KEYS_BLOCK_ITEM_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 165, 204, 162, 19, 99, 97, 140, 170, 232, 249, 53, 158, 10, 59, 38, 19, 113, 7, 74, 136, 193, 131, 104, 109, 224, 239, 155, 37, 89, 118, 108, 24, 52, 166, 201, 198, 124, 77, 217, 102, 50, 209, 218, 203, 131, 19, 36, 59, 136, 146, 242, 137, 40, 92, 238, 104, 29, 47, 171, 104, 168, 192, 200, 6, 1, 0, 64, 151, 113, 228, 228, 2, 117, 223, 77, 210, 86, 20, 142, 196, 243, 97, 103, 206, 194, 251, 211, 141, 208, 168, 74, 65, 213, 126, 85, 167, 246, 75, 16, 37, 178, 72, 246, 83, 10, 202, 44, 149, 25, 249, 34, 212, 100, 254, 19, 13, 193, 2, 148, 126, 191, 238, 133, 65, 172, 182, 66, 48, 9, 8, 4, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 11, 129, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};

    @SneakyThrows
    @Test
    public void testUpdateCredentialKeys() {
        Map<Index, ED25519PublicKey> keys = new HashMap<>();
        ED25519PublicKey newKey = ED25519PublicKey.from("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        AccountTransaction tx = TransactionFactory
                .newUpdateCredentialKeys()
                .credentialRegistrationID(regId)
                .keys(credentialPublicKeys)
                .numExistingCredentials(UInt16.from(5))
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(525))
                .expiry(Expiry.from(1669466666))
                .signer(TransactionSigner.from(
                        SignerEntry.from(Index.from(0), Index.from(0),
                                ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                        SignerEntry.from(Index.from(0), Index.from(1),
                                ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                ))
                .build();
        val payload = tx.getPayload();

        val transferBytesLength = payload.getBytes().length;
        assertEquals(85, transferBytesLength);

        val transferSignData = AccountTransaction.getDataToSign(tx.getHeader(), payload);
        assertEquals("4bc4ba29186577608baabc97c1f9df9cc37014aaa66f0dc300b91540fe4a2098", Hex.encodeHexString(transferSignData));

        val blockItemBytes = tx.getBytes();
        assertArrayEquals(EXPECTED_UPDATE_CREDENTIAL_KEYS_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = tx.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_WITH_UPDATE_CREDENTIAL_KEYS_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = tx.getHash();
        assertEquals("4c0d29171c4de386db2ef544e7c9d323745c09265943ca9d5e89deeebf607056", blockItemHash.asHex());
    }
}
