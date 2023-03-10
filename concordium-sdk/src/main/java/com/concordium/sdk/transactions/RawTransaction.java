package com.concordium.sdk.transactions;


import com.concordium.sdk.crypto.SHA256;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Arrays;

/**
 * A raw transaction
 */
@RequiredArgsConstructor
public class RawTransaction implements Transaction {

    @Getter
    private final int version;

    /**
     * The raw transaction bytes
     */
    private final byte[] bytes;

    /**
     * Transaction serialized as bytes.
     * How a transaction is serialized to bytes depends on type of the transaction. See {@link BlockItemType}.
     * <p>These bytes are not sent directly to the node. For that {@link Transaction#getBytes()} is used.</p>
     *
     * @return Serialized bytes of this Transaction.
     */
    @Override
    public byte[] getTransactionRequestPayloadBytes() {
        return bytes;
    }

    @Override
    public Hash getHash() {
        // skip first byte as this indicates a version which is not part of the actual hash.
        return Hash.from(SHA256.hash(bytes));
    }

    @Override
    public int getNetworkId() {
        return Transaction.DEFAULT_NETWORK_ID;
    }

    /**
     * Create a {@link Transaction} from raw bytes
     *
     * @param versionedTransactionBytes the raw transaction bytes with version prefixed.
     * @return the resulting {@link Transaction}
     */
    public static Transaction from(byte[] versionedTransactionBytes) {
        val versionByte = versionedTransactionBytes[0];
        val bytes = Arrays.copyOfRange(versionedTransactionBytes, 1, versionedTransactionBytes.length);

        return new RawTransaction((int) versionByte, bytes);
    }
}
