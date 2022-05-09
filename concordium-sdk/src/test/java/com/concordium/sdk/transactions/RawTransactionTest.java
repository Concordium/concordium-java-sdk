package com.concordium.sdk.transactions;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static com.concordium.sdk.transactions.TestUtils.signedByteArrayToUnsigned;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RawTransactionTest {

    @SneakyThrows
    @Test
    public void testRawTransactionCorrespondsToATransfer(){
        val transfer = TransferTransaction
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .receiver(AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM"))
                .amount(CCDAmount.fromMicro(17))
                .nonce(AccountNonce.from(78910))
                .expiry(Expiry.from(123456))
                .signer(TransactionTestHelper.getValidSigner())
                .build();
        assertArrayEquals(TestUtils.EXPECTED_BLOCK_ITEM_VERSIONED_BYTES, signedByteArrayToUnsigned(transfer.getBytes()));
        assertEquals("6a209eab54720aad71370a6adb4f0661d3606fca25ac544dc0ac0e76e099feba", transfer.getHash().asHex());

        val rawTransaction = RawTransaction.from(transfer.getBytes());

        assertArrayEquals(transfer.getBytes(), rawTransaction.getBytes());
        assertEquals(transfer.getNetworkId(), rawTransaction.getNetworkId());
        assertEquals(transfer.getHash(), rawTransaction.getHash());
    }
}
