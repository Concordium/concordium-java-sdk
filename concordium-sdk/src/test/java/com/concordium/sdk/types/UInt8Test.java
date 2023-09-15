package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class UInt8Test {

    @Test
    public void testCreation() {
        checkNegativeNumber();
        UInt8.from("255"); //(2^8)-1
        checkTooLargeNumber();
    }


    private void checkNegativeNumber() {
        try {
            UInt8.from(-13);
            fail("Should not be able to create negative UInt8");
        } catch (RuntimeException e) {
            if (!e.getMessage().equals("Value of UInt8 cannot be negative")) {
                fail("Unexpected error when creating negative UInt8 " + e.getMessage());
            }
        }
    }

    private void checkTooLargeNumber() {
        try {
            UInt8.from("256"); //(2^8)
            fail("This should exceed the range.");
        } catch (NumberFormatException e) {
            if (!e.getMessage().equals("Value of UInt8 cannot exceed 2^8")) {
                fail("Unexpected error when creating too big UInt8: " + e.getMessage());
            }
        }
    }

    @Test
    public void testSerializeDeserialize() {
        val expected = UInt8.from("250");
        val bytes = expected.getBytes();
        val deserialized = UInt8.from(bytes);
        assertEquals(expected, deserialized);
        assertNotEquals(expected, UInt8.from("42"));
    }
}
