package com.concordium.sdk.responsetypes;

import com.concordium.sdk.transactions.BlockHash;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BlockHashTest {
    @Test
    public void createBlockHashFromHex() {
        try {
            val hexHash = "3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1";
            val blockHash = BlockHash.from(hexHash);
            val hash = blockHash.getHash();
            assertEquals(hexHash, hash);
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }

    }
}
