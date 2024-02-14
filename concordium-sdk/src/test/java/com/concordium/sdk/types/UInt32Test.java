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
    public void testSerializeDeserialize() {
        val expected = UInt64.from("123242112");
        val bytes = expected.getBytes();
        val deserialized = UInt64.from(bytes);
        assertEquals(expected, deserialized);
        assertNotEquals(expected, UInt64.from("42"));
    }
}
