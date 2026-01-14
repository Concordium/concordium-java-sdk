package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static com.concordium.sdk.transactions.TestUtils.signedByteArrayToUnsigned;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RawTransactionTest {

    @SneakyThrows
    @Test
    public void testRawTransactionCorrespondsToATransfer() {
        val transfer = TransactionFactory
                .newTransfer(
                        Transfer
                                .builder()
                                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                                .amount(CCDAmount.fromMicro(17))
                                .build()
                )
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .sign(TransactionTestHelper.getValidSigner());
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, signedByteArrayToUnsigned(transfer.getVersionedBytes()));
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", transfer.getHash().asHex());

        val rawTransaction = RawTransaction.from(transfer.getVersionedBytes());

        assertArrayEquals(transfer.getVersionedBytes(), rawTransaction.getVersionedBytes());
        assertEquals(transfer.getNetworkId(), rawTransaction.getNetworkId());
        assertEquals(transfer.getHash(), rawTransaction.getHash());
    }
}
