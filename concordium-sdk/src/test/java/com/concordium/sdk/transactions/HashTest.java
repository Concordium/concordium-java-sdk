package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HashTest {
    @Test
    public void createHashFromBlockHash() {
        try {
            val hexHash = "3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
            val blockHash = Hash.from(hexHash);
            val hash = blockHash.asHex();
            assertEquals(hexHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void createHashFromTransactionHash() {
        try {
            val transactionHash = "78674107c228958752170db61c5a74929e990440d5da25975c6c6853f98db674";
            val txHash = Hash.from(transactionHash);
            val hash = txHash.asHex();
            assertEquals(transactionHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}
