package com.concordium.sdk.transactions;


import com.concordium.sdk.crypto.SHA256;

/**
 * A raw transaction
 */
public class RawTransaction implements Transaction {

    /**
     * The raw transaction bytes
     */
    private final byte[] bytes;

    RawTransaction(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public Hash getHash() {
        // skip first byte as this indicates a version which is not part of the actual hash.
        return Hash.from(SHA256.hash(this.bytes, 1, this.bytes.length - 1));
    }

    @Override
    public int getNetworkId() {
        return Transaction.DEFAULT_NETWORK_ID;
    }

    /**
     * Create a {@link Transaction} from raw bytes
     *
     * @param rawTransactionBytes the raw transaction bytes.
     * @return the resulting {@link Transaction}
     */
    public static Transaction from(byte[] rawTransactionBytes) {
        return new RawTransaction(rawTransactionBytes);
    }
}
