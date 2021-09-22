package com.concordium.sdk.types;


import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt64;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class UInt16Test {

    @Test
    public void testCreation() {
        checkNegativeNumber();
        UInt16.from("65535"); //(2^16)-1
        checkTooLargeNumber();
    }

    private void checkNegativeNumber() {
        try {
            UInt16.from((short) -13);
            fail("Should not be able to create a negative UInt16");
        }catch (RuntimeException e) {
            if (!e.getMessage().equals("Value of UInt16 cannot be negative")) {
                fail("Unexpected error when creating UInt16: " + e.getMessage());
            }
        }
    }

    private void checkTooLargeNumber() {
        try {
            UInt16.from("65536"); //(2^16)
            fail("This should exceed the range.");
        }catch (NumberFormatException e) {
            if(!e.getMessage().equals("Value of UInt16 cannot exceed 2^16")) {
                fail("Unexpected error when creating too big UInt16: " + e.getMessage());
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
