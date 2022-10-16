package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.accountinfo.credential.Key;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.val;
import java.nio.ByteBuffer;
import java.util.Map;

@Getter
@ToString
public class CredentialPublicKeys {
    private final Map<Index, Key> keys;
    private final int threshold;

    CredentialPublicKeys(Map<Index, Key> keys,
                         int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }

    public static CredentialPublicKeys from(Map<Index, Key> keys, int threshold) {
        return new CredentialPublicKeys(keys, threshold);
    }

    @SneakyThrows
    public byte[] getBytes() {
        val keysLenBytes = keys.keySet().size();
        int keyBufferSize = TransactionType.BYTES;
        for (Index key : keys.keySet())
        {
            Key value = keys.get(key);
            keyBufferSize += TransactionType.BYTES + value.getBytes().length;
        }
        val buffer = ByteBuffer.allocate(
                        keyBufferSize +
                                TransactionType.BYTES);
        buffer.put((byte)keysLenBytes);
        for (Index key : keys.keySet())
        {
            Key value = keys.get(key);
            buffer.put(key.getValue());
            buffer.put(value.getBytes());
        }

        buffer.put((byte)threshold);
        return buffer.array();
    }
}
