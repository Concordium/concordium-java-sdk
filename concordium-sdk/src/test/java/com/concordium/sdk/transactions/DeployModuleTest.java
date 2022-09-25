package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DeployModuleTest {
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES = {0, 1, 0, 2, 0, 0, 64, 77, 162, 178, 149, 56, 14, 141, 55, 252, 254, 54, 187, 73, 8, 39, 50, 153, 19, 138, 224, 61, 116, 251, 230, 120, 132, 53, 192, 53, 232, 149, 147, 180, 111, 169, 142, 101, 69, 209, 173, 171, 5, 212, 57, 71, 218, 230, 206, 201, 33, 135, 107, 239, 194, 44, 198, 194, 138, 111, 122, 8, 241, 219, 0, 1, 0, 64, 17, 49, 253, 7, 229, 21, 147, 193, 21, 47, 64, 49, 167, 113, 83, 126, 225, 74, 178, 178, 95, 89, 0, 187, 0, 195, 179, 44, 253, 78, 197, 158, 205, 205, 162, 82, 129, 242, 47, 240, 6, 141, 12, 6, 84, 177, 201, 115, 69, 88, 103, 154, 249, 37, 251, 42, 231, 128, 223, 221, 5, 226, 98, 2, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 149, 0, 0, 0, 33, 0, 0, 0, 0, 0, 1, 226, 64, 0, 0, 0, 0, 1, 0, 0, 0, 24, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7};
    final static int[] EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES = {0, 0, 1, 0, 2, 0, 0, 64, 77, 162, 178, 149, 56, 14, 141, 55, 252, 254, 54, 187, 73, 8, 39, 50, 153, 19, 138, 224, 61, 116, 251, 230, 120, 132, 53, 192, 53, 232, 149, 147, 180, 111, 169, 142, 101, 69, 209, 173, 171, 5, 212, 57, 71, 218, 230, 206, 201, 33, 135, 107, 239, 194, 44, 198, 194, 138, 111, 122, 8, 241, 219, 0, 1, 0, 64, 17, 49, 253, 7, 229, 21, 147, 193, 21, 47, 64, 49, 167, 113, 83, 126, 225, 74, 178, 178, 95, 89, 0, 187, 0, 195, 179, 44, 253, 78, 197, 158, 205, 205, 162, 82, 129, 242, 47, 240, 6, 141, 12, 6, 84, 177, 201, 115, 69, 88, 103, 154, 249, 37, 251, 42, 231, 128, 223, 221, 5, 226, 98, 2, 48, 29, 107, 23, 16, 181, 115, 90, 252, 36, 88, 152, 1, 33, 61, 19, 170, 107, 68, 120, 137, 15, 223, 232, 25, 91, 202, 14, 175, 34, 97, 78, 0, 0, 0, 0, 0, 1, 52, 62, 0, 0, 0, 0, 0, 0, 24, 149, 0, 0, 0, 33, 0, 0, 0, 0, 0, 1, 226, 64, 0, 0, 0, 0, 1, 0, 0, 0, 24, 175, 64, 182, 104, 145, 119, 15, 205, 34, 217, 38, 169, 28, 174, 177, 19, 92, 124, 231, 161, 186, 151, 124, 7};

    @SneakyThrows
    @Test
    public void testDeployModule() {
        Hash mod_ref = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
        val transfer = DeployModule.createNew(
                        WasmModule.from(mod_ref.getBytes(), 1), UInt64.from(6000))
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
        assertEquals(33, transferBytesLength);

        val transferSignedData = transfer.getDataToSign();
        assertEquals("90d700d331090fc406b199cc8964c581d490c25a54ab9f8e203cad343d6f2461", Hex.encodeHexString(transferSignedData));

        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemBytes = blockItem.getBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemBytes));

        val blockItemVersionedBytesBytes = blockItem.getVersionedBytes();
        assertArrayEquals(EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItemVersionedBytesBytes));

        val blockItemHash = blockItem.getHash();
        assertEquals("3d3b737524e6ea34bfb8c8dd35c446cdabead721d62fc8f0f26ae02e442f5daa", blockItemHash.asHex());
    }
}
