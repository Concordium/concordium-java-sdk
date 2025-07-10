package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class UInt32Test {

    @Test
    public void testCreation() {
        UInt32.from("4294967295"); //(2^32)-1
        checkTooLargeNumber();
    }

    private void checkTooLargeNumber() {
        try {
            UInt32.from("4294967296"); //(2^32)
            fail("This should exceed the range.");
        } catch (NumberFormatException e) {
            if (!e.getMessage().equals("String value 4294967296 exceeds range of unsigned int.")) {
                fail("Unexpected error when creating too big UInt32: " + e.getMessage());
            }
        }
    }

    @Test
    public void testComparable() {
        assertEquals(1, UInt32.from("4294967295").compareTo(UInt32.from(0)));
        assertEquals(0, UInt32.from("4294967295").compareTo(UInt32.from("4294967295")));
        assertEquals(-1, UInt32.from("4294967294").compareTo(UInt32.from("4294967295")));
    }

    @Test
    public void testAdding() {
        assertEquals(
                5,
                UInt32.from(3).plus(UInt32.from(2)).getValue()
        );
        // UInt32 max
        assertEquals(
                -1,
                UInt32.from(Integer.MIN_VALUE).plus(UInt32.from(Integer.MAX_VALUE)).getValue()
        );
        // Overflow
        assertEquals(
                0,
                UInt32.from(Integer.MIN_VALUE).plus(UInt32.from(Integer.MIN_VALUE)).getValue()
        );
    }

    @Test
    public void testSerializeDeserialize() {
        val expected = UInt32.from("123242112");
        val bytes = expected.getBytes();
        val deserialized = UInt32.from(bytes);
        assertEquals(expected, deserialized);
        assertNotEquals(expected, UInt32.from("42"));
    }
}
