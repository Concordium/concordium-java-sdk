package com.concordium.sdk.cis2;

import com.concordium.sdk.types.LEB128U;
import lombok.*;

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
     * @throws IllegalArgumentException if the resulting byte array would exceed 37 bytes.
     */
    public byte[] encode() {
        try {
            return LEB128U.encode(this.amount, 37);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid encoding of TokenAmount. Must not exceed 37 byes.", e);
        }

    }

    /**
     * Deserialize a {@link TokenAmount} from the provided buffer.
     * This function assumes that the token amounts are LEB128U encoded.
     *
     * @param buffer the buffer to read from.
     * @return the parsed {@link TokenAmount}
     * @throws IllegalArgumentException if the encoding is more than 37 bytes.
     */
    public static TokenAmount decode(ByteBuffer buffer) {

        try {
            val result = LEB128U.decode(buffer, 37);
            return new TokenAmount(result);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tried to decode a TokenAmount consisting of more than 37 bytes.", e);
        }
    }
}



