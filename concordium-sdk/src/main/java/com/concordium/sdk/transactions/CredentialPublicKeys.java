package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * The public credential keys belonging to a credential holder
 */
@Getter
@ToString
public class CredentialPublicKeys {

    /**
     * Credential keys (i.e. account holder keys).
     */
    private final Map<Index, ED25519PublicKey> keys;
    /**
     * The account threshold.
     */
    private final int threshold;

    CredentialPublicKeys(Map<Index, ED25519PublicKey> keys,
                         int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }

    /**
     * Creates a new `CredentialPublicKeys` object with the given keys and threshold.
     *
     * @param keys      the credential keys (i.e. account holder keys).
     * @param threshold the account threshold.
     * @return a new `CredentialPublicKeys` object with the given keys and threshold.
     */
    public static CredentialPublicKeys from(Map<Index, ED25519PublicKey> keys, int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }
        return new CredentialPublicKeys(keys, threshold);
    }

    byte[] getSchemeIdBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES);
        buffer.put((byte) 0);
        return buffer.array();
    }

    public byte[] getBytes() {
        val keysLenBytes = keys.keySet().size();
        int keyBufferSize = TransactionType.BYTES;
        val schemeIdBytes = getSchemeIdBytes();
        for (Index key : keys.keySet()) {
            ED25519PublicKey value = keys.get(key);
            keyBufferSize += TransactionType.BYTES + value.getBytes().length + TransactionType.BYTES;
        }
        val buffer = ByteBuffer.allocate(
                keyBufferSize +
                        TransactionType.BYTES);
        buffer.put((byte) keysLenBytes);
        for (Index key : keys.keySet()) {
            ED25519PublicKey value = keys.get(key);
            buffer.put(schemeIdBytes);
            buffer.put(key.getValue());
            buffer.put(value.getBytes());
        }

        buffer.put((byte) threshold);
        return buffer.array();
    }
}
