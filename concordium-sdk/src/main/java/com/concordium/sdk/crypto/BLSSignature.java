package com.concordium.sdk.crypto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;

@EqualsAndHashCode
@ToString(includeFieldNames = false)
public class BLSSignature {

    private static final int FIXED_LENGTH = 48;

    /**
     * The bytes representing the raw aggregate signature. The bytes have a fixed length of {@value BLSSignature#FIXED_LENGTH} bytes.
     */
    private final byte[] bytes;

    private BLSSignature(final byte[] bytes) {
        this.bytes = bytes;
    }

    @Builder
    public static BLSSignature from(final byte[] sig) {
        if (! (sig.length == 48)) {throw new IllegalArgumentException("BLSSignature must have fixed length of " + FIXED_LENGTH + " bytes"); }
        return new BLSSignature(sig);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }
}
