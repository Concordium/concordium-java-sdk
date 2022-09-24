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

public class InitContractTest {
    @SneakyThrows
    @Test
    public void testInitContract() {
        Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
        val transfer = InitContract.createNew(
                        InitContractPayload.from(
                                0,
                                moduleRef.getBytes(),
                                "CIS2-NFT",
                                new byte[0]),
                        UInt64.from(3000))
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

        assertEquals(53, transfer.getBytes().length);
        assertEquals("9574239a5a4c13c6b59704ea2fc2f4b6dc075e4c6b8c28bf9a5b2e5adb50fa99", Hex.encodeHexString(transfer.getDataToSign()));
        val blockItem = transfer.toAccountTransaction().toBlockItem();

        val blockItemHash = blockItem.getHash();
        assertArrayEquals(TestUtils.EXPECTED_INIT_CONTRACT_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getBytes()));
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_TRANSFER_WITH_INIT_CONTRACT_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        assertEquals("07499104dbee5b98e23c87b739d61a57493438c3f90f185a24b53902135ea4bc", blockItemHash.asHex());
    }
}
