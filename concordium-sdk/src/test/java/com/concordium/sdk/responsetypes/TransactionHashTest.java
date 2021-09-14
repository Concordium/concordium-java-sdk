package com.concordium.sdk.responsetypes;

import com.concordium.sdk.transactions.TransactionHash;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransactionHashTest {

    @Test
    public void createTransactionHashFromHex() {
        try {
            val transactionHash = "78674107c228958752170db61c5a74929e990440d5da25975c6c6853f98db674";
            val txHash = TransactionHash.from(transactionHash);
            val hash = txHash.getHash();
            assertEquals(transactionHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
