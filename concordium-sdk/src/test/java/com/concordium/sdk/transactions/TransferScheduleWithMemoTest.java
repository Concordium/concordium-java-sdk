package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransferScheduleWithMemoTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 172, 84, 78, 17, 114, 41, 94, 104, 97, 101, 208, 56, 191, 40, 194, 82, 154, 234, 83, 78, 68, 121, 249, 237, 87, 136, 120, 160, 72, 34, 220, 122, 188, 92, 115, 59, 180, 134, 253, 25, 5, 241, 107, 227, 140, 130, 135, 185, 240, 134, 235, 179, 19, 4, 177, 189, 209, 128, 105, 227, 178, 183, 104, 7, 1, 0, 64, 131, 174, 190, 98, 44, 84, 244, 228, 62, 88, 172, 142, 248, 73, 203, 29, 33, 73, 94, 90, 126, 90, 205, 62, 182, 11, 15, 200, 240, 10, 175, 133, 91, 206, 187, 105, 89, 237, 53, 247, 235, 76, 132, 65, 93, 90, 193, 1, 88, 207, 43, 212, 201, 253, 100, 41, 97, 246, 148, 232, 93, 45, 91, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 173, 0, 0, 0, 57, 0, 0, 0, 0, 0, 1, 226, 64, 24, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 0, 5, 1, 2, 3, 4, 5, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 172, 84, 78, 17, 114, 41, 94, 104, 97, 101, 208, 56, 191, 40, 194, 82, 154, 234, 83, 78, 68, 121, 249, 237, 87, 136, 120, 160, 72, 34, 220, 122, 188, 92, 115, 59, 180, 134, 253, 25, 5, 241, 107, 227, 140, 130, 135, 185, 240, 134, 235, 179, 19, 4, 177, 189, 209, 128, 105, 227, 178, 183, 104, 7, 1, 0, 64, 131, 174, 190, 98, 44, 84, 244, 228, 62, 88, 172, 142, 248, 73, 203, 29, 33, 73, 94, 90, 126, 90, 205, 62, 182, 11, 15, 200, 240, 10, 175, 133, 91, 206, 187, 105, 89, 237, 53, 247, 235, 76, 132, 65, 93, 90, 193, 1, 88, 207, 43, 212, 201, 253, 100, 41, 97, 246, 148, 232, 93, 45, 91, 8, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 173, 0, 0, 0, 57, 0, 0, 0, 0, 0, 1, 226, 64, 24, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 0, 5, 1, 2, 3, 4, 5, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @SneakyThrows
    @Test
    public void testTransferScheduleWithMemo() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(1662869154000L, 10);
        val transfer = TransferScheduleWithMemo.createNew(
                        AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"), schedule, Memo.from(new byte[]{1, 2, 3, 4, 5}), UInt64.from(6000))
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
        assertEquals(57, transferBytesLength);

        val transferSignedData = transfer.getDataToSign();
        assertEquals("3aaf0b1aac60c3259b2af48cbb7d9eb4c16b9bcdb866f2a73ed59dc5988ac491", Hex.encodeHexString(transferSignedData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("b2add6a79132b1575c85766f3f45dcd917894080f2d90ce31d0cfa6949ea39bd", blockItemHash.asHex());
    }
}
