package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionSignature {
    private final Map<Byte, Map<Byte, byte[]>> signatures = new HashMap<>();

    void put(Byte credentialIndex, Byte keyIndex, byte[] signature) {
        if (Objects.isNull(signatures.get(credentialIndex))) {
            signatures.put(credentialIndex, new HashMap<>());
        }
        signatures.get(credentialIndex).put(keyIndex, signature);
    }

    byte[] getBytes() {
        val buffer = getByteBuffer();
        buffer.put((byte) signatures.size());
        for (val credentialIndex : signatures.keySet()) {
            buffer.put(credentialIndex);
            val keySignatureMap = signatures.get(credentialIndex);
            buffer.put((byte) keySignatureMap.size());
            for (Byte keyIdx : keySignatureMap.keySet()) {
                buffer.put(keyIdx);
                buffer.put(UInt16.from((short) keySignatureMap.get(keyIdx).length).getBytes());
                buffer.put(keySignatureMap.get(keyIdx));
            }
        }
        return buffer.array();
    }

    private ByteBuffer getByteBuffer() {
        int size = OUTER_LENGTH;
        for (Map<Byte, byte[]> value : signatures.values()) {
            size += INNER_LENGTH + CREDENTIAL_INDEX;
            for (Byte ignored : value.keySet()) {
                size += KEY_INDEX + SIGNATURE_LENGTH + SIGNATURE_SIZE;
            }
        }
        return ByteBuffer.allocate(size);
    }

    private static final int OUTER_LENGTH = 1;
    private static final int CREDENTIAL_INDEX = 1;
    private static final int INNER_LENGTH = 1;
    private static final int KEY_INDEX = 1;
    private static final int SIGNATURE_LENGTH = 2;
    private static final int SIGNATURE_SIZE = 64;
}
