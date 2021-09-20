package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.fail;

public class MemoTest {

    @Test
    public void testCreateMemo() {
        try {
            val memo = Memo.from(new byte[256]);
        } catch (TransactionCreationException e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void testCreateInvalidMemos() {
        testWithTooLargeMemo();
    }

    private void testWithTooLargeMemo() {
        try {
            val memo = Memo.from(new byte[257]);
            fail("Memo should not have been created.");
        } catch (TransactionCreationException e) {
            if (!e.getMessage().equals("The creation of the Transaction failed. Size of memo cannot exceed 256 bytes")) {
                fail("Unexpected error: " + e.getMessage());
            }
        }
    }

}
