package com.concordium.sdk.transactions;

import com.concordium.sdk.transactions.UInt32;
import com.concordium.sdk.transactions.UInt64;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class UInt32Test {

    @Test
    public void testCreation() {
        checkNegativeNumber();
        UInt32.from("4294967295"); //(2^32)-1
        checkTooLargeNumber();
    }

    private void checkNegativeNumber() {
        try {
            UInt32.from(-13);
            fail("Should not be able to create a negative UInt32");
        }catch (RuntimeException e) {
            if (!e.getMessage().equals("Value of UInt32 can not be negative")) {
                fail("Unexpected error when creating UInt32: " + e.getMessage());
            }
        }
    }

    private void checkTooLargeNumber() {
        try {
            UInt32.from("4294967296"); //(2^32)
            fail("This should exceed the range.");
        }catch (NumberFormatException e) {
            if(!e.getMessage().equals("String value 4294967296 exceeds range of unsigned int.")) {
                fail("Unexpected error when creating too big UInt32: " + e.getMessage());
            }
        }
    }

    @Test
    public void testSerializeDeserialize() {
        val expected = UInt64.from("123242112");
        val bytes = expected.getBytes();
        val deserialized = UInt64.from(bytes);
        assertEquals(expected, deserialized);
        assertNotEquals(expected, UInt64.from("42"));
    }
}
