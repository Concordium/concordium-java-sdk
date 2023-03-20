package com.concordium.sdk.transactions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionSignatureTest {
    @Test
    public void testGetBytes() {
        assertEquals(70, TransactionSignature
                .builder()
                .signature(Index.from(0), TransactionSignatureAccountSignatureMap
                        .builder()
                        .signature(Index.from(0), Signature.from(new byte[64]))
                        .build())
                .build()
                .getBytes()
                .length);


        assertEquals(70 + 69, TransactionSignature
                .builder()
                .signature(Index.from(0), TransactionSignatureAccountSignatureMap
                        .builder()
                        .signature(Index.from(0), Signature.from(new byte[64]))
                        .build())
                .signature(Index.from(1), TransactionSignatureAccountSignatureMap
                        .builder()
                        .signature(Index.from(2), Signature.from(new byte[64]))
                        .build())
                .build()
                .getBytes()
                .length);

        assertEquals(70 + 69 + 67, TransactionSignature
                .builder()
                .signature(Index.from(0), TransactionSignatureAccountSignatureMap
                        .builder()
                        .signature(Index.from(0), Signature.from(new byte[64]))
                        .build())
                .signature(Index.from(1), TransactionSignatureAccountSignatureMap
                        .builder()
                        .signature(Index.from(2), Signature.from(new byte[64]))
                        .signature(Index.from(3), Signature.from(new byte[64]))
                        .build())
                .build()
                .getBytes()
                .length);
    }
}
