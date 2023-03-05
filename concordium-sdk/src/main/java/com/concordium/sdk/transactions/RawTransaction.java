package com.concordium.sdk.transactions;


import com.concordium.sdk.crypto.SHA256;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A raw transaction
 */
@Getter
@RequiredArgsConstructor
public class RawTransaction implements Transaction {

    private final int version;

    /**
     * The raw transaction bytes
     */
    private final byte[] bytes;

    @Override
    public Hash getHash() {
        // skip first byte as this indicates a version which is not part of the actual hash.
        return Hash.from(SHA256.hash(bytes));
    }

    @Override
    public int getNetworkId() {
        return Transaction.DEFAULT_NETWORK_ID;
    }

    @Override
    public byte[] getVersionedBytes() {
        val bytes = getBytes();
        val buffer = ByteBuffer.allocate(VERSION_SIZE + bytes.length);
        buffer.put((byte) getVersion());
        buffer.put(bytes);

        return buffer.array();
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
