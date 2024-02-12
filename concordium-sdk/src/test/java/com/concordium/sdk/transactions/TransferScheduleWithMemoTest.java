package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransferScheduleWithMemoTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 227, 101, 142, 211, 119, 229, 153, 4, 50, 157, 74, 129, 135, 10, 85, 230, 194, 4, 193, 49, 226, 178, 183, 193, 198, 53, 205, 237, 131, 139, 121, 97, 27, 247, 48, 91, 117, 101, 133, 242, 8, 250, 152, 51, 248, 86, 42, 127, 155, 139, 8, 198, 95, 212, 32, 247, 253, 203, 120, 46, 2, 74, 228, 11, 1, 0, 64, 155, 84, 226, 5, 111, 249, 21, 62, 126, 83, 130, 221, 192, 186, 97, 101, 167, 17, 168, 83, 58, 62, 185, 71, 24, 42, 101, 193, 210, 112, 145, 25, 189, 196, 181, 158, 62, 23, 42, 50, 96, 9, 182, 56, 45, 228, 4, 225, 76, 141, 104, 81, 202, 166, 176, 236, 212, 170, 177, 226, 204, 103, 39, 13, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 2, 169, 0, 0, 0, 57, 0, 0, 0, 0, 0, 1, 226, 64, 24, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 0, 5, 1, 2, 3, 4, 5, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 227, 101, 142, 211, 119, 229, 153, 4, 50, 157, 74, 129, 135, 10, 85, 230, 194, 4, 193, 49, 226, 178, 183, 193, 198, 53, 205, 237, 131, 139, 121, 97, 27, 247, 48, 91, 117, 101, 133, 242, 8, 250, 152, 51, 248, 86, 42, 127, 155, 139, 8, 198, 95, 212, 32, 247, 253, 203, 120, 46, 2, 74, 228, 11, 1, 0, 64, 155, 84, 226, 5, 111, 249, 21, 62, 126, 83, 130, 221, 192, 186, 97, 101, 167, 17, 168, 83, 58, 62, 185, 71, 24, 42, 101, 193, 210, 112, 145, 25, 189, 196, 181, 158, 62, 23, 42, 50, 96, 9, 182, 56, 45, 228, 4, 225, 76, 141, 104, 81, 202, 166, 176, 236, 212, 170, 177, 226, 204, 103, 39, 13, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 2, 169, 0, 0, 0, 57, 0, 0, 0, 0, 0, 1, 226, 64, 24, 86, 218, 213, 202, 227, 118, 122, 189, 60, 41, 95, 79, 16, 12, 128, 53, 126, 218, 213, 46, 65, 151, 20, 245, 216, 43, 188, 125, 208, 225, 4, 133, 0, 5, 1, 2, 3, 4, 5, 1, 0, 0, 1, 131, 42, 185, 168, 208, 0, 0, 0, 0, 0, 0, 0, 10};

    @SneakyThrows
    @Test
    public void testTransferScheduleWithMemo() {
        Schedule[] schedule = new Schedule[1];
        schedule[0] = Schedule.from(Timestamp.newMillis(1662869154000L), 10);
        val tx = TransactionFactory.newScheduledTransferWithMemo()
                .schedule(schedule)
                .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
                .memo(Memo.from(new byte[]{1, 2, 3, 4, 5}))
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

        val payload = tx.getPayload();

        val transferBytesLength = payload.getBytes().length;
        assertEquals(57, transferBytesLength);

        val transferSignedData = payload.getDataToSign();
        assertEquals("d1ca550b56c40614e0ecfe91b690ebe393e32b0b2ebae5d3a58f6bf8bbeb363d", Hex.encodeHexString(transferSignedData));

        val blockItem = payload.toAccountTransaction();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("9fe5011512b33f45095b95cf731765648f6047b1c7c87da9be2ed41e4292bef5", blockItemHash.asHex());
    }
}
