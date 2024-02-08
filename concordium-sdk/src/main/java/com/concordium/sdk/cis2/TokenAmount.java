package com.concordium.sdk.cis2;

import lombok.*;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * An amount as specified in the CIS2 specification.
 * https://proposals.concordium.software/CIS/cis-2.html#tokenamount
 * <p>
 * It is an unsigned integer where the max value is 2^256 - 1.
 */
@EqualsAndHashCode
@ToString
public class TokenAmount {

    /**
     * The maximum value of a CIS2 token as per the standard 2^256 - 1.
     */
    public static final BigInteger MAX_VALUE = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);

    @Getter
    private final BigInteger amount;

    private TokenAmount(BigInteger value) {
        if (value.compareTo(MAX_VALUE) > 0) throw new IllegalArgumentException("TokenAmount exceeds max value");
        this.amount = value;
    }

    public static TokenAmount from(long value) {
        return new TokenAmount(BigInteger.valueOf(value));
    }

    public static TokenAmount from(String value) {
        if (value.startsWith("-")) throw new IllegalArgumentException("TokenAmount must be positive");
        return new TokenAmount(new BigInteger(value));
    }

    /**
     * Encode the {@link TokenAmount} in LEB128 unsigned format.
     *
     * @return the serialized token amount
     * @throws RuntimeException if the resulting byte array would exceed 37 bytes.
     */
    @SneakyThrows
    public byte[] encode() {
        if (this.amount.equals(BigInteger.ZERO)) return new byte[]{0};
        val bos = new ByteArrayOutputStream();
        var value = this.amount;
        // Loop until the most significant byte is zero or less
        while (value.compareTo(BigInteger.ZERO) > 0) {
            // Take the 7 least significant bits of the current value and set the MSB
            var currentByte = value.and(BigInteger.valueOf(0x7F)).byteValue();
            value = value.shiftRight(7);
            if (value.compareTo(BigInteger.ZERO) != 0) {
                currentByte |= 0x80; // Set the MSB to 1 to indicate there are more bytes to come
            }
            bos.write(currentByte);
        }
        val result = bos.toByteArray();
        if (result.length > 37) throw new IllegalArgumentException("Invalid encoding of TokenAmount. Must not exceed 37 byes.");
        return result;

    }

    /**
     * Deserialize a {@link TokenAmount} from the provided buffer.
     * This function assumes that the token amounts are LEB128U encoded.
     *
     * @param buffer the buffer to read from.
     * @return the parsed {@link TokenAmount}
     * @throws RuntimeException if the encoding is more than 37 bytes.
     */
    public static TokenAmount decode(ByteBuffer buffer) {
        var result = BigInteger.ZERO;
        int shift = 0;
        int count = 0;
        while (true) {
            if (count > 37) throw new IllegalArgumentException("Tried to decode a TokenAmount which consists of more than 37 bytes.");
            byte b = buffer.get();
            BigInteger byteValue = BigInteger.valueOf(b & 0x7F); // Mask to get 7 least significant bits
            result = result.or(byteValue.shiftLeft(shift));
            if ((b & 0x80) == 0) {
                break; // If MSB is 0, this is the last byte
            }
            shift += 7;
            count++;
        }
        return new TokenAmount(result);
    }


}
