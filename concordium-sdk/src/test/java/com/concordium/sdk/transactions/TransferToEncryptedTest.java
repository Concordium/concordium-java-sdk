package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransferToEncryptedTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 104, 249, 233, 109, 254, 77, 199, 74, 254, 178, 255, 165, 30, 208, 2, 22, 146, 70, 152, 178, 29, 13, 136, 19, 71, 46, 125, 191, 244, 188, 181, 161, 10, 219, 15, 70, 136, 81, 208, 27, 162, 154, 181, 31, 21, 240, 228, 74, 238, 135, 49, 58, 43, 182, 29, 103, 142, 239, 62, 17, 177, 114, 190, 6, 1, 0, 64, 145, 86, 157, 26, 95, 60, 15, 26, 47, 31, 252, 147, 162, 151, 6, 84, 161, 124, 42, 59, 138, 174, 114, 18, 29, 217, 109, 229, 251, 222, 128, 56, 93, 98, 200, 48, 166, 7, 183, 23, 179, 140, 146, 248, 72, 85, 25, 75, 119, 165, 169, 78, 144, 14, 218, 30, 118, 45, 165, 113, 192, 130, 65, 13, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 125, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 104, 249, 233, 109, 254, 77, 199, 74, 254, 178, 255, 165, 30, 208, 2, 22, 146, 70, 152, 178, 29, 13, 136, 19, 71, 46, 125, 191, 244, 188, 181, 161, 10, 219, 15, 70, 136, 81, 208, 27, 162, 154, 181, 31, 21, 240, 228, 74, 238, 135, 49, 58, 43, 182, 29, 103, 142, 239, 62, 17, 177, 114, 190, 6, 1, 0, 64, 145, 86, 157, 26, 95, 60, 15, 26, 47, 31, 252, 147, 162, 151, 6, 84, 161, 124, 42, 59, 138, 174, 114, 18, 29, 217, 109, 229, 251, 222, 128, 56, 93, 98, 200, 48, 166, 7, 183, 23, 179, 140, 146, 248, 72, 85, 25, 75, 119, 165, 169, 78, 144, 14, 218, 30, 118, 45, 165, 113, 192, 130, 65, 13, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 125, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};

    @SneakyThrows
    @Test
    public void testDeployModule() {
        val transfer = TransferToEncrypted.createNew(
                        TransferToEncryptedPayload.from(1), UInt64.from(6000))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(Nonce.from(78910))
                        .expiry(UInt64.from(123456))
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
        assertEquals(9, transferBytesLength);

        val transferSignedData = transfer.getDataToSign();
        assertEquals("aec03d4d831d904be8588d3177fa49ee488fd38e47bc76623b8ba5bebb4eaf76", Hex.encodeHexString(transferSignedData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("db89b84d1b049d2d841258b6356ba31bcb5f36dc2060f410f5e88c79db13f176", blockItemHash.asHex());
    }
}
