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

public class UpdateContractTest {
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_DATA_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 233, 169, 11, 190, 208, 168, 208, 238, 232, 42, 100, 182, 241, 165, 179, 169, 47, 50, 173, 204, 29, 125, 197, 68, 171, 78, 94, 33, 109, 83, 224, 255, 92, 94, 131, 36, 79, 31, 36, 255, 132, 251, 82, 209, 64, 153, 116, 231, 150, 86, 143, 40, 32, 29, 124, 13, 186, 38, 140, 67, 244, 125, 87, 4, 1, 0, 64, 214, 53, 165, 255, 152, 21, 203, 243, 108, 161, 197, 183, 122, 154, 17, 13, 18, 239, 183, 125, 69, 102, 149, 51, 207, 252, 149, 75, 45, 145, 65, 152, 159, 206, 239, 201, 128, 55, 39, 122, 4, 82, 2, 167, 39, 95, 25, 153, 23, 133, 194, 9, 187, 58, 84, 23, 21, 73, 125, 235, 159, 13, 14, 1, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 230, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
    final static int[] EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_VERSIONED_DATA_BYTES = new int[]{0, 1, 0, 2, 0, 0, 64, 233, 169, 11, 190, 208, 168, 208, 238, 232, 42, 100, 182, 241, 165, 179, 169, 47, 50, 173, 204, 29, 125, 197, 68, 171, 78, 94, 33, 109, 83, 224, 255, 92, 94, 131, 36, 79, 31, 36, 255, 132, 251, 82, 209, 64, 153, 116, 231, 150, 86, 143, 40, 32, 29, 124, 13, 186, 38, 140, 67, 244, 125, 87, 4, 1, 0, 64, 214, 53, 165, 255, 152, 21, 203, 243, 108, 161, 197, 183, 122, 154, 17, 13, 18, 239, 183, 125, 69, 102, 149, 51, 207, 252, 149, 75, 45, 145, 65, 152, 159, 206, 239, 201, 128, 55, 39, 122, 4, 82, 2, 167, 39, 95, 25, 153, 23, 133, 194, 9, 187, 58, 84, 23, 21, 73, 125, 235, 159, 13, 14, 1, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 12, 230, 0, 0, 0, 42, 0, 0, 0, 0, 0, 1, 226, 64, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 13, 67, 73, 83, 50, 45, 78, 70, 84, 46, 109, 105, 110, 116, 0, 0};
    @SneakyThrows
    @Test
    public void updateContractTest() {
        byte[] emptyArray = new byte[0];
        val transfer = UpdateContract.createNew(
                        UpdateContractPayload.from(0, ContractAddress.from(81, 0), "CIS2-NFT", "mint", emptyArray), UInt64.from(3000))
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
        assertEquals(42, transferBytesLength);

        val transferDataToSign = transfer.getDataToSign();
        assertEquals("e7cdef6d48303783ebda487180ddbad10ab6ab0bcd9717320e3c40062dc7de30", Hex.encodeHexString(transferDataToSign));
        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("dd3c030d6d5122ba333283273719719c00b415bc57f5a3c629aa5bdb21502ab1", blockItemHash.asHex());
    }
}
