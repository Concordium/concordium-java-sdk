package com.concordium.sdk.transactions;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IndexTest {

    @Test
    public void testCreatingKeyIndexes() {
        checkNegativeKeyIndexFails();
        checkCanCreateKeyIndex();
        checkCanNotCreateTooLargeKeyIndex();
    }

    private void checkCanCreateKeyIndex() {
        val keyIndex = Index.from(255);
        val asByte = keyIndex.getValue();
        assertEquals(255, asByte & 0xFF); // signed byte to "unsigned byte" hidden in an int.
    }

    private void checkCanNotCreateTooLargeKeyIndex() {
        try {
            Index.from(256);
            fail("Should have failed");
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().equals("KeyIndex cannot exceed one byte (255) was 256")) {
                fail("Unexpected error when creating KeyIndex: " + e.getMessage());
            }
        }
    }

    private void checkNegativeKeyIndexFails() {
        try {
            Index.from(-1);
            fail("Should have failed");
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().equals("KeyIndex cannot be negative")) {
                fail("Unexpected error when creating KeyIndex: " + e.getMessage());
            }
        }
    }
}
