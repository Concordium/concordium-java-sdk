package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UInt64Test {

    @Test
    public void testCreation() {
        checkNegativeNumber();
        UInt64.from("18446744073709551615"); //(2^64)-1
        checkTooLargeNumber();
    }

    private void checkNegativeNumber() {
        try {
            UInt64.from(-13);
            fail("Should not be able to create a negative UInt64");
        }catch (RuntimeException e) {
            if (!e.getMessage().equals("Value of UInt64 can not be negative")) {
                fail("Unexpected error when creating UInt64: " + e.getMessage());
            }
        }
    }

    private void checkTooLargeNumber() {
        try {
            UInt64.from("18446744073709551616"); //(2^64)
            fail("This should exceed the range.");
        }catch (NumberFormatException e) {
            if(!e.getMessage().equals("String value 18446744073709551616 exceeds range of unsigned long.")) {
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

}
