package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class TransactionHeaderTest {
    @Test
    public void testGetBytes() {
        val header = TransactionHeader
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .nonce(Nonce.from(78910))
                .expiry(Expiry.from(123456))
                .payloadSize(UInt32.from(41))
                .maxEnergyCost(UInt64.from(610))
                .build();

        val headerBytes = header.getBytes();
        Assert.assertEquals(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES, headerBytes.length);
    }
}
