package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class TransferWithMemoTest {

    @Test
    public void testCreateTransferWithMemo() {
        try {
            AccountTransaction tx = TransactionFactory.newTransferWithMemo()
                    .memo(Memo.from(new byte[]{1, 2, 3, 4, 5}))
                    .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                    .amount(CCDAmount.fromMicro(17))
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
            val transferWithMemo = tx.getPayload();

            assertEquals(UInt64.from(608), tx.getHeader().getMaxEnergyCost());
            assertEquals(48, transferWithMemo.getBytes().length);

            assertEquals(Hash.from("2cf00d7d5064ab6f70102a8bba4082b7d85b9b411f981f00b5994adc0b461083"), tx.getHash());
            assertEquals(tx.getHash(), BlockItem.fromVersionedBytes(ByteBuffer.wrap(tx.getVersionedBytes())).getHash());
            assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_TRANSFER_WITH_MEMO_VERSIONED_BYTES, TestUtils.signedByteArrayToUnsigned(tx.getVersionedBytes()));
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
