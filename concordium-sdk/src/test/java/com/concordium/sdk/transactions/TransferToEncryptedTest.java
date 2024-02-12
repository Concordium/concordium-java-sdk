package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransferToEncryptedTest {
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 211, 2, 167, 82, 142, 176, 115, 94, 113, 95, 191, 154, 75, 170, 201, 213, 243, 148, 198, 153, 199, 92, 20, 215, 193, 37, 176, 213, 196, 146, 108, 58, 131, 14, 107, 60, 252, 126, 253, 177, 238, 119, 246, 219, 136, 3, 152, 113, 96, 21, 66, 195, 115, 129, 53, 57, 89, 209, 39, 120, 204, 84, 80, 0, 1, 0, 64, 153, 204, 108, 39, 70, 219, 155, 177, 31, 8, 67, 109, 30, 168, 25, 247, 160, 151, 114, 193, 132, 142, 153, 182, 131, 111, 81, 177, 250, 189, 98, 190, 179, 202, 139, 225, 233, 81, 147, 240, 7, 59, 48, 3, 231, 231, 228, 182, 159, 5, 169, 101, 123, 166, 154, 31, 44, 102, 109, 183, 27, 142, 114, 4, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 3, 101, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};
    final static int[] EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 211, 2, 167, 82, 142, 176, 115, 94, 113, 95, 191, 154, 75, 170, 201, 213, 243, 148, 198, 153, 199, 92, 20, 215, 193, 37, 176, 213, 196, 146, 108, 58, 131, 14, 107, 60, 252, 126, 253, 177, 238, 119, 246, 219, 136, 3, 152, 113, 96, 21, 66, 195, 115, 129, 53, 57, 89, 209, 39, 120, 204, 84, 80, 0, 1, 0, 64, 153, 204, 108, 39, 70, 219, 155, 177, 31, 8, 67, 109, 30, 168, 25, 247, 160, 151, 114, 193, 132, 142, 153, 182, 131, 111, 81, 177, 250, 189, 98, 190, 179, 202, 139, 225, 233, 81, 147, 240, 7, 59, 48, 3, 231, 231, 228, 182, 159, 5, 169, 101, 123, 166, 154, 31, 44, 102, 109, 183, 27, 142, 114, 4, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 3, 101, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 226, 64, 17, 0, 0, 0, 0, 0, 0, 0, 1};

    @SneakyThrows
    @Test
    public void testTransferToEncrypted() {
        TransferToEncryptedTransaction tx = TransactionFactory.newTransferToEncrypted()
                .amount(CCDAmount.fromMicro(1))
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionSigner.from(
                        SignerEntry.from(Index.from(0), Index.from(0),
                                ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                        SignerEntry.from(Index.from(0), Index.from(1),
                                ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                ))
                .build();
        val transfer = tx.getPayload();
        val transferBytesLength = transfer.getBytes().length;
        assertEquals(9, transferBytesLength);

        val transferSignedData = transfer.getDataToSign();
        assertEquals("95435c82b0cf38c4294e3d197fd7845c00348893549c1318f4965142da591b1f", Hex.encodeHexString(transferSignedData));

        val blockItem = transfer.toAccountTransaction();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_TRANSFER_TO_ENCRYPTED_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("e777705bb7d8ecd020f1a261049b3f0020d4032026ba6fdbf311f2871a26688e", blockItemHash.asHex());
    }
}
