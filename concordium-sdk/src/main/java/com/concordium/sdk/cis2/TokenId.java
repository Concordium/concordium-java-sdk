package com.concordium.sdk.cis2;

import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A simple wrapper around the actual token id.
 * https://proposals.concordium.software/CIS/cis-2.html#tokenid
 * The token id simply consists of 0 or more bytes.
 */
@EqualsAndHashCode
@Getter
public class TokenId {

    private static final int MAX_BYTES_SIZE = 256;

    private final byte[] bytes;

    private TokenId(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Construct a minimum token id i.e., the empty one.
     * @return the token id
     */
    public static TokenId min() {
        return new TokenId(new byte[0]);
    }

    /**
     * Get the size of the token (in number of bytes)
     * @return the size of the token
     */
    public int getSize() {
        return this.bytes.length;
    }

    /**
     * Create a token from the provided byte array
     * @param bytes the token id in raw bytes
     * @return the token id
     */
    public static TokenId from(byte[] bytes) {
        if (bytes.length > MAX_BYTES_SIZE) {
            throw new IllegalArgumentException("TokenId supersedes max allowed size.");
        }
        return new TokenId(bytes);
    }

    /**
     * Create a token id that consists of two bytes
     * @param value the token id
     * @return a new token id
     */
    public static TokenId from(UInt16 value) {
        return new TokenId(value.getBytes());
    }

    /**
     * Create a token id that consists of four bytes
     * @param value the token id
     * @return a new token id
     */
    public static TokenId from(UInt32 value) {
        return new TokenId(value.getBytes());
    }

    /**
     * Create a token id that consists of eight bytes
     * @param value the token id
     * @return a new token id
     */
    public static TokenId from(UInt64 value) {
        return new TokenId(value.getBytes());
    }

}
