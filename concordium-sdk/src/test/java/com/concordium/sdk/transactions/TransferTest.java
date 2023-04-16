package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TransferTest {
    @SneakyThrows
    @Test
    public void testCreateTransfer() {
        val transfer = Transfer.createNew(
                        AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                        CCDAmount.fromMicro(17))
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

        assertEquals(41, transfer.getBytes().length);
        assertEquals(601, transfer.header.getMaxEnergyCost().getValue());
        assertEquals("60afc40624ba9c9698efb5f49cae32810bce082b08bd55ca625d63f3e4dd56a2", Hex.encodeHexString(transfer.getDataToSign()));
        val blockItem = transfer.toAccountTransaction();

        val blockItemHash = blockItem.getHash();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getBytes()));
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        assertEquals(blockItem.getHash(), BlockItem.fromVersionedBytes(ByteBuffer.wrap(blockItem.getVersionedBytes())).getHash());
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", blockItemHash.asHex());
    }
}
