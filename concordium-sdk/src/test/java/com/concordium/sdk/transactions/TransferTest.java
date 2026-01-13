package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
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
        AccountTransaction tx = TransactionFactory.newTransfer()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .signer(TransactionSigner.from(
                        SignerEntry.from(Index.from(0), Index.from(0),
                                ED25519SecretKey.from("7100071c835a0a35e86dccba7ee9d10b89e36d1e596771cdc8ee36a17f7abbf2")),
                        SignerEntry.from(Index.from(0), Index.from(1),
                                ED25519SecretKey.from("cd20ea0127cddf77cf2c20a18ec4516a99528a72e642ac7deb92131a9d108ae9"))
                ))
                .build();
        val transfer = tx.getPayload();

        assertEquals(41, transfer.getBytes().length);
        assertEquals(601, tx.getHeader().getMaxEnergyCost().getValue());
        assertEquals("60afc40624ba9c9698efb5f49cae32810bce082b08bd55ca625d63f3e4dd56a2",
                Hex.encodeHexString(AccountTransaction.getDataToSign(tx.getHeader(), transfer)));

        val blockItemHash = tx.getHash();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_BYTES, TestUtils.signedByteArrayToUnsigned(tx.getBytes()));
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(tx.getVersionedBytes()));
        assertEquals(tx.getHash(), BlockItem.fromVersionedBytes(ByteBuffer.wrap(tx.getVersionedBytes())).getHash());
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", blockItemHash.asHex());
    }

    @Test
    public void testDeserializeTransfer() {
        val transfer = new Transfer(
                AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"),
                CCDAmount.fromMicro(17));
        val deserializedTransfer = Transfer.fromBytes(ByteBuffer.wrap(transfer.getPayloadBytes()));
        assertEquals(transfer, deserializedTransfer);
    }
}
