package com.concordium.sdk.crypto;

/**
 * Interface for keys exposed in a raw format i.e. a byte array.
 */
public interface RawKey {

    byte[] getRawBytes();
}
