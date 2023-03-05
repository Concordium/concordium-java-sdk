package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import lombok.val;

import java.nio.ByteBuffer;

public interface Transaction {
    int DEFAULT_NETWORK_ID = 100;
    // todo: in practice this is true, but in the future this could different as the version is variable length encoded.
    int VERSION_SIZE = 1;
    int VERSION = 0;

    /**
     * Returns serialized {@link Transaction}
     * This is the raw bytes that is sent to the node.
     *
     * @return The serialized {@link Transaction}
     */
    default byte[] getVersionedBytes() {
        val bytes = getBytes();
        val buffer = ByteBuffer.allocate(VERSION_SIZE + bytes.length);
        buffer.put((byte) VERSION);
        buffer.put(bytes);

        return buffer.array();
    }

    byte[] getBytes();

    /**
     * Get the {@link Hash} of the {@link Transaction}
     *
     * @return the hash
     */
    default Hash getHash() {
        return Hash.from(SHA256.hash(getBytes()));
    }

    /**
     * The network id.
     * The default value is {@link Transaction#DEFAULT_NETWORK_ID}
     *
     * @return the network id.
     */
    default int getNetworkId() {
        return DEFAULT_NETWORK_ID;
    }

}
