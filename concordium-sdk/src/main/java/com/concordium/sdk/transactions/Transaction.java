package com.concordium.sdk.transactions;

public interface Transaction {
    /**
     * Returns serialized {@link Transaction}
     * This is the raw bytes that is sent to the node.
     * @return The serialized {@link Transaction}
     */
    byte[] getBytes();

    /**
     * Get the {@link Hash} of the {@link Transaction}
     * @return the hash
     */
    Hash getHash();

    /**
     * The network id.
     * The default value is {@link Transaction#DEFAULT_NETWORK_ID}
     * @return the network id.
     */
    int getNetworkId();

    int DEFAULT_NETWORK_ID = 100;
}
