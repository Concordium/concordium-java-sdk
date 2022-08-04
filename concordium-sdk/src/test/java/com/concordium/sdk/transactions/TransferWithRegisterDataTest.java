package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class TransferWithRegisterDataTest {

    @Test
    public void testCreateTransferWithRegisterData() {
        try {
            val transferWithRegisterData = RegisterData.createNew(
                            Data.from(new byte[]{1, 2, 3, 4, 5}))
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

            assertEquals(UInt64.from(568), transferWithRegisterData.header.getMaxEnergyCost());
            assertEquals(8, transferWithRegisterData.getBytes().length);

            val blockItem = transferWithRegisterData.toBlockItem();
            assertEquals(Hash.from("0ad44be061cbdfc22fdcf14e2cd48d7c34d543ea60cb9cf5298cb40d89c25d83"), blockItem.getHash());
            assertEquals(blockItem.getHash(), BlockItem.fromVersionedBytes(ByteBuffer.wrap(blockItem.getVersionedBytes())).getHash());
            assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_TRANSFER_WITH_REGISTER_DATA_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(blockItem.getVersionedBytes()));
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
