package com.concordium.sdk.serializing;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ByteArraysTest {

    @Test
    public void testConcat() {
        val array0 = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0};
        val array1 = new byte[]{0x1, 0x1, 0x1, 0x1, 0x1};
        val array2 = new byte[]{0x2, 0x1, 0x2, 0x1, 0x2};
        val expected = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x1, 0x1, 0x1, 0x2, 0x1, 0x2, 0x1, 0x2};

        val result = ByteArrays.concat(array0, array1, array2);
        assertEquals(array0.length + array1.length + array2.length, result.length);
        assertArrayEquals(expected, result);
    }
}
