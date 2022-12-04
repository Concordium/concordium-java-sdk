package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.responses.accountinfo.credential.Key;
import com.concordium.sdk.types.Nonce;
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
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_WITH_UPDATE_CREDENTIAL_KEYS_VERSIONED_BYTES = new int[]{0, 0, 1, 0, 2, 0, 0, 64, 26, 144, 19, 221, 36, 199, 194, 70, 50, 125, 199, 225, 117, 173, 122, 136, 116, 194, 202, 124, 159, 54, 91, 186, 67, 163, 163, 49, 17, 3, 63, 50, 251, 85, 148, 203, 137, 98, 61, 155, 184, 107, 137, 74, 104, 199, 101, 254, 132, 90, 147, 185, 113, 77, 5, 216, 40, 193, 126, 163, 140, 6, 37, 9, 1, 0, 64, 231, 121, 250, 229, 28, 140, 225, 33, 236, 72, 35, 132, 14, 249, 42, 192, 151, 195, 240, 31, 18, 68, 153, 188, 102, 77, 235, 65, 63, 211, 254, 181, 139, 23, 81, 157, 59, 250, 171, 56, 179, 88, 120, 221, 49, 244, 140, 166, 199, 180, 13, 201, 73, 136, 38, 204, 87, 177, 111, 110, 220, 177, 249, 1, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 13, 17, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};
    final static int[] EXPECTED_UPDATE_CREDENTIAL_KEYS_BLOCK_ITEM_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 26, 144, 19, 221, 36, 199, 194, 70, 50, 125, 199, 225, 117, 173, 122, 136, 116, 194, 202, 124, 159, 54, 91, 186, 67, 163, 163, 49, 17, 3, 63, 50, 251, 85, 148, 203, 137, 98, 61, 155, 184, 107, 137, 74, 104, 199, 101, 254, 132, 90, 147, 185, 113, 77, 5, 216, 40, 193, 126, 163, 140, 6, 37, 9, 1, 0, 64, 231, 121, 250, 229, 28, 140, 225, 33, 236, 72, 35, 132, 14, 249, 42, 192, 151, 195, 240, 31, 18, 68, 153, 188, 102, 77, 235, 65, 63, 211, 254, 181, 139, 23, 81, 157, 59, 250, 171, 56, 179, 88, 120, 221, 49, 244, 140, 166, 199, 180, 13, 201, 73, 136, 38, 204, 87, 177, 111, 110, 220, 177, 249, 1, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 0, 2, 13, 0, 0, 0, 0, 0, 0, 13, 17, 0, 0, 0, 85, 0, 0, 0, 0, 99, 130, 10, 42, 13, 166, 67, 214, 8, 42, 143, 128, 70, 15, 255, 39, 243, 255, 39, 254, 219, 253, 198, 0, 57, 82, 116, 2, 184, 24, 143, 200, 69, 168, 73, 66, 139, 84, 132, 200, 42, 21, 137, 202, 183, 96, 76, 26, 43, 233, 120, 195, 156, 1, 0, 0, 173, 101, 145, 162, 222, 176, 60, 50, 53, 118, 21, 215, 62, 20, 78, 1, 164, 154, 186, 212, 150, 113, 66, 141, 70, 219, 88, 207, 45, 78, 77, 135, 1};

    @SneakyThrows
    @Test
    public void testUpdateCredentialKeys() {
        Map<Index, Key> keys =  new HashMap<>();
        Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
        keys.put(Index.from(0), newKey);

        CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
        CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);

        val transfer = UpdateCredentialKeys.createNew(
                        regId,
                        credentialPublicKeys,
                        UInt64.from(3000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(525))
                        .expiry(UInt64.from(1669466666))
                        .build())
                .signWith(
                        TransactionSigner.from(
                                SignerEntry.from(Index.from(0), Index.from(0),
                                        ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                                SignerEntry.from(Index.from(0), Index.from(1),
                                        ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                        )
                );

        val transferBytesLength = transfer.getBytes().length;
        assertEquals(85, transferBytesLength);

        val transferSignData = transfer.getDataToSign();
        assertEquals("1e6cf18027b59c3d89eda60bf0adbf76a9b80f5c4c9d82400133844d15b342a9", Hex.encodeHexString(transferSignData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_UPDATE_CREDENTIAL_KEYS_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_WITH_UPDATE_CREDENTIAL_KEYS_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("8c089b961c37950b64444b77bb33e58004ea7650a12144187326d8e21192a58a", blockItemHash.asHex());
    }
}
