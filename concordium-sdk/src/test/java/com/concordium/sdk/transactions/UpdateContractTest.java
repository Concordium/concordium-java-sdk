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
        assertEquals(42, transfer.getBytes().length);
        assertEquals("02b15fc092aa1e63035a18cb1b4f62a9d2a6186bdb6147f85525bcb8771ab5b7", Hex.encodeHexString(transfer.getDataToSign()));
        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemHash = blockItem.getHash();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getBytes()));
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_UPDATE_CONTRACT_VERSIONED_DATA_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        assertEquals("0a869928f2491d0652a708eac0231582e24c0cc036481f209862147856531d20", blockItemHash.asHex());
    }
}
