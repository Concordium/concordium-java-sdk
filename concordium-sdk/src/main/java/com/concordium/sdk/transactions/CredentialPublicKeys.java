package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import lombok.Getter;
import lombok.SneakyThrows;
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
    private final Map<Index, ED25519PublicKey> keys;
    private final int threshold;

    CredentialPublicKeys(Map<Index, ED25519PublicKey> keys,
                         int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }

    public static CredentialPublicKeys from(Map<Index, ED25519PublicKey> keys, int threshold) {
        return new CredentialPublicKeys(keys, threshold);
    }

    byte[] getSchemeIdBytes() {
        val buffer = ByteBuffer.allocate(PublicKey.BYTES);
        buffer.put(PublicKey.ED25519.getValue());
        return buffer.array();
    }

    @SneakyThrows
    public byte[] getBytes() {
        val keysLenBytes = keys.keySet().size();
        int keyBufferSize = TransactionType.BYTES;
        val schemeIdBytes = getSchemeIdBytes();
        for (Index key : keys.keySet())
        {
            ED25519PublicKey value = keys.get(key);
            keyBufferSize += TransactionType.BYTES + value.getBytes().length + PublicKey.BYTES;
        }
        val buffer = ByteBuffer.allocate(
                        keyBufferSize +
                                TransactionType.BYTES);
        buffer.put((byte)keysLenBytes);
        for (Index key : keys.keySet())
        {
            ED25519PublicKey value = keys.get(key);
            buffer.put(schemeIdBytes);
            buffer.put(key.getValue());
            buffer.put(value.getBytes());
        }

        buffer.put((byte)threshold);
        return buffer.array();
    }
}
