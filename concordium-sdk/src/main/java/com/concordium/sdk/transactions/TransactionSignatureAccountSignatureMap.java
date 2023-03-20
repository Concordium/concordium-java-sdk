package com.concordium.sdk.transactions;

import com.google.common.collect.ImmutableSortedMap;
import lombok.*;

import java.util.Set;

/**
 * Represents signatures for a single Account in {@link TransactionSignature}.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class TransactionSignatureAccountSignatureMap {
    /**
     * Signatures.
     */
    @Singular
    private final ImmutableSortedMap<Index, Signature> signatures;

    public int size() {
        return signatures.size();
    }

    public Set<Index> keySet() {
        return signatures.keySet();
    }

    public Signature get(final Index keyIdx) {
        return signatures.get(keyIdx);
    }
}
