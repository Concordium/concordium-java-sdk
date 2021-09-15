package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionSignatureTest {
    @Test
    public void testGetBytes() {
        val transactionSignature = new TransactionSignature();
        transactionSignature.put(Index.from(0), Index.from(0), new byte[64]);
        assertEquals(70, transactionSignature.getBytes().length);
        transactionSignature.put(Index.from(1), Index.from(2), new byte[64]);
        assertEquals(70+69, transactionSignature.getBytes().length);
        transactionSignature.put(Index.from(1), Index.from(3), new byte[64]);
        assertEquals(70+69+67, transactionSignature.getBytes().length);
    }
}
