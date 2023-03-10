package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.google.common.collect.ImmutableSortedMap;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * Represents Signature for an {@link AccountTransaction}.
 * An {@link AccountTransaction} is signed by using {@link TransactionSigner}.
 */
@ToString
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class TransactionSignature {
    @Singular
    private final ImmutableSortedMap<Index, TransactionSignatureAccountSignatureMap> signatures;

    byte[] getBytes() {
        val buffer = getByteBuffer();
        buffer.put((byte) signatures.size());

        for (val credentialIndex : signatures.keySet()) {
            buffer.put(credentialIndex.getValue());
            val keySignatureMap = signatures.get(credentialIndex);
            buffer.put((byte) keySignatureMap.size());
            for (Index keyIdx : keySignatureMap.keySet()) {
                buffer.put(keyIdx.getValue());
                buffer.put(UInt16.from((short) keySignatureMap.get(keyIdx).getLength()).getBytes());
                buffer.put(keySignatureMap.get(keyIdx).getBytes());
            }
        }

        return buffer.array();
    }

    public static TransactionSignature fromBytes(ByteBuffer source) {
        byte outerLen = source.get();
        var builder = TransactionSignature.builder();
        for (byte outerCount = 0; outerCount < outerLen; ++outerCount) {
            final Index credIdx = Index.fromBytes(source);
            byte innerLen = source.get();
            var builderInternal
                    = TransactionSignatureAccountSignatureMap.builder();
            for (byte innerCount = 0; innerCount < innerLen; ++innerCount) {
                Index keyIndex = Index.fromBytes(source);
                UInt16 sigLen = UInt16.fromBytes(source);
                // preallocating here is relatively safe since it can be at most 65kB.
                byte[] sig = new byte[sigLen.getValue()];
                source.get(sig);
                builderInternal.signature(keyIndex, Signature.from(sig));
            }
            builder.signature(credIdx, builderInternal.build());
        }

        return builder.build();
    }

    private ByteBuffer getByteBuffer() {
        int size = OUTER_LENGTH;
        for (TransactionSignatureAccountSignatureMap value : signatures.values()) {
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

