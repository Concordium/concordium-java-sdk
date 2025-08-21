package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UInt64Test {

    @Test
    public void testCreation() {
        UInt64.from("18446744073709551615"); //(2^64)-1
        checkTooLargeNumber();
    }


    private void checkTooLargeNumber() {
        try {
            UInt64.from("18446744073709551616"); //(2^64)
            fail("This should exceed the range.");
        } catch (NumberFormatException e) {
            if (!e.getMessage().equals("String value 18446744073709551616 exceeds range of unsigned long.")) {
                fail("Unexpected error when creating too big UInt64: " + e.getMessage());
            }
        }
    }

    @Test
    public void testSerializeDeserialize() {
        val expected = UInt64.from(123242112);
        val bytes = expected.getBytes();
        val deserialized = UInt64.from(bytes);
        assertEquals(expected, deserialized);
    }

    @Test
    public void testAdding() {
        assertEquals(
                5,
                UInt64.from(3).plus(UInt64.from(2)).getValue()
        );
        // UInt64 max
        assertEquals(
                -1L,
                UInt64.from(Long.MIN_VALUE).plus(UInt64.from(Long.MAX_VALUE)).getValue()
        );
        // Overflow
        assertEquals(
                0,
                UInt64.from(Long.MIN_VALUE).plus(UInt64.from(Long.MIN_VALUE)).getValue()
        );
    }
}
