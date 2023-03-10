package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;

import static com.google.common.primitives.Bytes.concat;

public interface Transaction {
    int DEFAULT_NETWORK_ID = 100;
    // todo: in practice this is true, but in the future this could different as the version is variable length encoded.
    int VERSION_SIZE = 1;
    int VERSION = 0;

    /**
     * Returns serialized {@link Transaction}
     * This is the raw bytes that is sent to the node.
     * It is a concatenation of the Transaction Version {@link Transaction#VERSION} + {@link Transaction#getTransactionRequestPayloadBytes()}
     *
     * @return The serialized {@link Transaction}
     */
    default byte[] getBytes() {
        return concat(new byte[]{(byte) VERSION}, getTransactionRequestPayloadBytes());
    }

    /**
     * Transaction serialized as bytes.
     * How a transaction is serialized to bytes depends on type of the transaction. See {@link BlockItemType}.
     * <p>These bytes are not sent directly to the node. For that {@link Transaction#getBytes()} is used.</p>
     *
     * @return Serialized bytes of this Transaction.
     */
    byte[] getTransactionRequestPayloadBytes();

    /**
     * Get the {@link Hash} of the {@link Transaction}
     *
     * @return the hash
     */
    default Hash getHash() {
        return Hash.from(SHA256.hash(getTransactionRequestPayloadBytes()));
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
