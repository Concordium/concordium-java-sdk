package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@EqualsAndHashCode
@ToString
public class TransactionSignature {
    private final Map<Index, Map<Index, byte[]>> signatures = new TreeMap<>();

    public void put(Index credentialIndex, Index index, byte[] signature) {
        if (Objects.isNull(signatures.get(credentialIndex))) {
            signatures.put(credentialIndex, new TreeMap<>());
        }
        signatures.get(credentialIndex).put(index, signature);
    }

    byte[] getBytes() {
        val buffer = getByteBuffer();
        buffer.put((byte) signatures.size());
        for (val credentialIndex : signatures.keySet()) {
            buffer.put(credentialIndex.getValue());
            val keySignatureMap = signatures.get(credentialIndex);
            buffer.put((byte) keySignatureMap.size());
            for (Index keyIdx : keySignatureMap.keySet()) {
                buffer.put(keyIdx.getValue());
                buffer.put(UInt16.from((short) keySignatureMap.get(keyIdx).length).getBytes());
                buffer.put(keySignatureMap.get(keyIdx));
            }
        }
        return buffer.array();
    }

    public static TransactionSignature fromBytes(ByteBuffer source) {
        byte outerLen = source.get();
        TransactionSignature signatures = new TransactionSignature();
        for (byte outerCount = 0; outerCount < outerLen; ++outerCount) {
            Index credIdx = Index.fromBytes(source);
            byte innerLen = source.get();
            for (byte innerCount = 0; innerCount < innerLen; ++innerCount) {
                Index keyIndex = Index.fromBytes(source);
                UInt16 sigLen = UInt16.fromBytes(source);
                // preallocating here is relatively safe since it can be at most 65kB.
                byte[] sig = new byte[sigLen.getValue()];
                source.get(sig);
                signatures.put(credIdx, keyIndex, sig);
            }
        }
        return signatures;
    }

    private ByteBuffer getByteBuffer() {
        int size = OUTER_LENGTH;
        for (Map<Index, byte[]> value : signatures.values()) {
            size += INNER_LENGTH + CREDENTIAL_INDEX;
            for (Index ignored : value.keySet()) {
                size += KEY_INDEX + SIGNATURE_LENGTH + SIGNATURE_SIZE;
            }
        }
        return ByteBuffer.allocate(size);
    }

    private static final int OUTER_LENGTH = 1;
    private static final int CREDENTIAL_INDEX = 1;
    private static final int INNER_LENGTH = 1;
    private static final int KEY_INDEX = 1;
    static final int SIGNATURE_LENGTH = 2;
    static final int SIGNATURE_SIZE = 64;
}
