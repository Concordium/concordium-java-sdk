package com.concordium.sdk.types;

import lombok.*;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Contains methods to encode/decode LEB128U amounts.
 */
public class LEB128U {

    /**
     * LEB128U integer of unbounded size.
     */
    public static int UNBOUNDED = -1;

    /**
     * Max number of bytes in a LEB128U encoded u64. ceil(64/7).
     */
    public static int U64_BYTES = 10;

    /**
     * Max number of bytes in a LEB128U encoded u32. ceil(32/7).
     */
    public static int U32_BYTES = 5;

    /**
     * Deserialize a LEB128U encoded value from the provided buffer.
     * Behaves like decode(buffer, LEB128U.UNBOUNDED).
     *
     * @param buffer the buffer to read from.
     * @return {@link BigInteger} representing the encoded value
     */
    public static BigInteger decode(ByteBuffer buffer) {
        return decode(buffer, UNBOUNDED);
    }

    /**
     * Deserialize a LEB128U encoded value from the provided buffer.
     *
     * @param buffer the buffer to read from.
     * @param maxSize the max amount of bytes to decode.
     * @return {@link BigInteger} representing the encoded value
     * @throws IllegalArgumentException if more than `maxSize` bytes are decoded.
     */
    public static BigInteger decode(ByteBuffer buffer, int maxSize) {
        var result = BigInteger.ZERO;
        int shift = 0;
        int count = 0;
        while (true) {
            byte b = buffer.get();
            BigInteger byteValue = BigInteger.valueOf(b & 0x7F); // Mask to get 7 least significant bits
            result = result.or(byteValue.shiftLeft(shift));
            if ((b & 0x80) == 0) {
                break; // If MSB is 0, this is the last byte
            }
            shift += 7;
            count++;
            if (maxSize != UNBOUNDED && count > maxSize) {
                throw new IllegalArgumentException("LEB128U encoded integer is larger than provided max size: " + maxSize);
            }
        }
        return result;
    }

    /**
     * Encode the provided {@link BigInteger} in LEB128 unsigned format.
     * Behaves like encode(value, LEB128U.UNBOUNDED).
     *
     * @param value {@link BigInteger} representing the value to encode.
     * @return byte array containing the encoded value.
     */
    public static byte[] encode(BigInteger value) {
        return encode(value, UNBOUNDED);
    }

    /**
     * Encode the provided {@link BigInteger} in LEB128 unsigned format.
     *
     * @param value {@link BigInteger} representing the value to encode.
     * @param maxSize the max amount of bytes to decode.
     * @return byte array containing the encoded value.
     * @throws IllegalArgumentException if more than `maxSize` bytes are encoded or `value` is negative.
     */
    public static byte[] encode(BigInteger value, int maxSize) {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot encode negative amount: " + value);
        }
        if (value.equals(BigInteger.ZERO)) {
            return new byte[]{0};
        }
        val bos = new ByteArrayOutputStream();
        var valueToEncode = value;
        // Loop until the most significant byte is zero or less
        while (valueToEncode.compareTo(BigInteger.ZERO) > 0) {
            // Take the 7 least significant bits of the current value and set the MSB
            var currentByte =  valueToEncode.and(BigInteger.valueOf(0x7F)).byteValue();
            valueToEncode = valueToEncode.shiftRight(7);
            if (valueToEncode.compareTo(BigInteger.ZERO) != 0) {
                currentByte |= 0x80; // Set the MSB to 1 to indicate there are more bytes to come
            }
            bos.write(currentByte);
            if (maxSize != UNBOUNDED && bos.size() > maxSize) {
                throw new IllegalArgumentException("BigInteger: " + value + " does not fit withing provided max size: " + maxSize);
            }
        }
        return bos.toByteArray();
    }
}
