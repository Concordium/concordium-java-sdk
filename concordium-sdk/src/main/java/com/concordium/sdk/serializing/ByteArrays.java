package com.concordium.sdk.serializing;

import lombok.val;

import java.nio.ByteBuffer;

public final class ByteArrays {

    public static byte[] concat(byte[] array, byte[]... arrays) {
        int size = array.length;
        for (byte[] bytes : arrays) {
            size += bytes.length;
        }
        val buffer = ByteBuffer.allocate(size);
        buffer.put(array);
        for (byte[] bytes : arrays) {
            buffer.put(bytes);
        }
        return buffer.array();
    }
}
