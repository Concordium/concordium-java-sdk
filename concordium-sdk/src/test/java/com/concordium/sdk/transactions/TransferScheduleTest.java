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

public class TransferScheduleTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 65, 213, 7, 96, 82, 98, 186, 208, 126, 234, 164, 180, 27, 239, 103, 52, 39, 101, 13, 83, 83, 12, 217, 132, 42, 106, 10, 245, 181, 43, 135, 27, 247, 63, 168, 155, 210, 192, 23, 120, 170, 48, 202, 147, 203, 126, 115, 42, 118, 211, 61, 207, 69, 3, 8, 40, 243, 208, 152, 93, 23, 153, 155, 7, 1, 0, 64, 132, 205, 150, 226, 108, 23, 20, 23, 51, 202, 229, 130, 134, 141, 83, 188, 61, 17, 229, 0, 86, 16, 123, 23, 101, 106, 238, 239, 250, 108, 16, 8, 61, 141, 180, 18, 208, 186, 32, 184, 228, 1, 6, 149, 196, 187, 240, 201, 115, 218, 146, 8, 174, 136, 154, 5, 108, 79, 68, 143, 63, 114, 203, 3, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 166, 0, 0, 0, 50, 0, 0, 0, 0, 0, 1, 226, 64, 19, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 65, 213, 7, 96, 82, 98, 186, 208, 126, 234, 164, 180, 27, 239, 103, 52, 39, 101, 13, 83, 83, 12, 217, 132, 42, 106, 10, 245, 181, 43, 135, 27, 247, 63, 168, 155, 210, 192, 23, 120, 170, 48, 202, 147, 203, 126, 115, 42, 118, 211, 61, 207, 69, 3, 8, 40, 243, 208, 152, 93, 23, 153, 155, 7, 1, 0, 64, 132, 205, 150, 226, 108, 23, 20, 23, 51, 202, 229, 130, 134, 141, 83, 188, 61, 17, 229, 0, 86, 16, 123, 23, 101, 106, 238, 239, 250, 108, 16, 8, 61, 141, 180, 18, 208, 186, 32, 184, 228, 1, 6, 149, 196, 187, 240, 201, 115, 218, 146, 8, 174, 136, 154, 5, 108, 79, 68, 143, 63, 114, 203, 3, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 166, 0, 0, 0, 50, 0, 0, 0, 0, 0, 1, 226, 64, 19, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @SneakyThrows
    @Test
    public void testTransferSchedule() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        val transfer = TransferSchedule.createNew(
                        AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"), schedule, UInt64.from(6000))
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
        assertEquals(50, transferBytesLength);

        val transferSignedData = transfer.getDataToSign();
        assertEquals("0defc70782c040856ba31a28dff6d2256cb51ad76153aadea2b678a7d7179566", Hex.encodeHexString(transferSignedData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("407196935979d887d9d2bb940440fa0fe2886d55920c7df3206aa4da2f16f0bc", blockItemHash.asHex());
    }
}
