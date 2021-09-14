package com.concordium.sdk.transactions;

import com.concordium.sdk.NativeResolver;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SimpleTransferTest {
    @Test
    public void testCreateSimpleTransfer() {
        NativeResolver.loadLib();
        val transfer = SimpleTransfer.makeNew(
                        Account.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                        UInt64.from(17))
                .withHeader(TransactionHeader
                        .builder()
                        .sender(Account.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                        .accountNonce(UInt64.from(78910))
                        .expiry(UInt64.from(123456))
                        .build())
                .withSigner(
                        new TransactionSigner()
                                .put((byte) 0, (byte) 0,
                                        ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2"))
                                .put((byte) 0, (byte) 1,
                                        ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                );

        assertEquals(41, transfer.getBytes().length);
        assertEquals(601, transfer.header.getMaxEnergyCost().getValue());
        assertEquals("60afc40624ba9c9698efb5f49cae32810bce082b08bd55ca625d63f3e4dd56a2", Hex.encodeHexString(transfer.getSignData()));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemHash = blockItem.getHash();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_BYTES, signedByteArrayToUnsigned(blockItem.getBytes()));
        assertArrayEquals(EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", blockItemHash.getHash());
    }

    private int[] signedByteArrayToUnsigned(byte[] bytes) {
        val out = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            out[i] = Byte.toUnsignedInt(bytes[i]);
        }
        return out;
    }

    private final static int[] EXPECTED_BLOCK_ITEM_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 152, 227, 127, 99, 157, 56, 42, 159, 2, 201, 153, 75, 203, 225, 32, 170, 250, 113, 86, 154, 221, 39, 67, 217, 104, 230, 252, 238, 144, 171, 148, 190, 180, 53, 148, 163, 156, 67, 200, 194, 171, 125, 50, 34, 7, 163, 16, 11, 174, 57, 32, 182, 39, 175, 140, 53, 221, 212, 247, 198, 238, 40, 172, 3, 1, 0, 64, 251, 92, 45, 163, 94, 136, 144, 50, 231, 36, 74, 36, 65, 31, 185, 11, 70, 69, 187, 0, 116, 221, 88, 132, 9, 75, 94, 146, 142, 38, 174, 165, 165, 91, 19, 187, 255, 120, 109, 91, 24, 238, 62, 101, 74, 138, 118, 2, 65, 136, 169, 90, 126, 198, 88, 34, 52, 67, 227, 217, 112, 174, 57, 10, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 2, 89, 0, 0, 0, 41, 0, 0, 0, 0, 0, 1, 226, 64, 3, 99, 115, 136, 188, 214, 204, 203, 57, 195, 36, 187, 150, 38, 82, 98, 202, 219, 128, 109, 136, 75, 54, 232, 84, 31, 221, 18, 246, 161, 193, 14, 225, 0, 0, 0, 0, 0, 0, 0, 17};
    private final static int[] EXPECTED_BLOCK_ITEM_VERSIONED_BYTES = new int[]{0, 0, 1, 0, 2, 0, 0, 64, 152, 227, 127, 99, 157, 56, 42, 159, 2, 201, 153, 75, 203, 225, 32, 170, 250, 113, 86, 154, 221, 39, 67, 217, 104, 230, 252, 238, 144, 171, 148, 190, 180, 53, 148, 163, 156, 67, 200, 194, 171, 125, 50, 34, 7, 163, 16, 11, 174, 57, 32, 182, 39, 175, 140, 53, 221, 212, 247, 198, 238, 40, 172, 3, 1, 0, 64, 251, 92, 45, 163, 94, 136, 144, 50, 231, 36, 74, 36, 65, 31, 185, 11, 70, 69, 187, 0, 116, 221, 88, 132, 9, 75, 94, 146, 142, 38, 174, 165, 165, 91, 19, 187, 255, 120, 109, 91, 24, 238, 62, 101, 74, 138, 118, 2, 65, 136, 169, 90, 126, 198, 88, 34, 52, 67, 227, 217, 112, 174, 57, 10, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 2, 89, 0, 0, 0, 41, 0, 0, 0, 0, 0, 1, 226, 64, 3, 99, 115, 136, 188, 214, 204, 203, 57, 195, 36, 187, 150, 38, 82, 98, 202, 219, 128, 109, 136, 75, 54, 232, 84, 31, 221, 18, 246, 161, 193, 14, 225, 0, 0, 0, 0, 0, 0, 0, 17};
}
