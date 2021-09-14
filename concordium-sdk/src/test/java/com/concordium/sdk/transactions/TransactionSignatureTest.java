package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionSignatureTest {
    @Test
    public void testGetBytes() {
        val transactionSignature = new TransactionSignature();
        transactionSignature.put((byte) 0, (byte) 0, new byte[64]);
        assertEquals(70, transactionSignature.getBytes().length);
        transactionSignature.put((byte) 1, (byte) 2, new byte[64]);
        assertEquals(70+69, transactionSignature.getBytes().length);
        transactionSignature.put((byte) 1, (byte) 3, new byte[64]);
        assertEquals(70+69+67, transactionSignature.getBytes().length);
    }
}
