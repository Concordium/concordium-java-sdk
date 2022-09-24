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
        assertEquals(33, transfer.getBytes().length);
        assertEquals("90d700d331090fc406b199cc8964c581d490c25a54ab9f8e203cad343d6f2461", Hex.encodeHexString(transfer.getDataToSign()));
        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemHash = blockItem.getHash();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getBytes()));
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_DEPLOY_MODULE_VERSIONED_DATA_BYTES_2, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        assertEquals("3d3b737524e6ea34bfb8c8dd35c446cdabead721d62fc8f0f26ae02e442f5daa", blockItemHash.asHex());
    }
}
