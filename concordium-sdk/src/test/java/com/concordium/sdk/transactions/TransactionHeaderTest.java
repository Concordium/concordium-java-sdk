package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.*;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionHeaderTest {
    @Test
    public void testGetBytes() {
        val header = TransactionHeader
                .builder()
                .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
                .accountNonce(UInt64.from(78910))
                .expiry(UInt64.from(123456))
                .build();
        header.setPayloadSize(UInt32.from(41));
        header.setMaxEnergyCost(UInt64.from(610));

        val headerBytes = header.getBytes();
        Assert.assertEquals(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES, headerBytes.length);
    }
}
