package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.UpdateKeysIndex;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.types.UInt32;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Keys allowed to update a chain parameter.
 */
@ToString
@EqualsAndHashCode
@Getter
public class AccessStructure {
    /**
     * Keys allowed to sign.
     */
    private final Set<Index> keys;

    /**
     * The required threshold of signers before
     * the chain update is enqueued.
     */
    private final UInt32 threshold;

    public AccessStructure(Set<Index> keys, UInt32 threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }

    public static AccessStructure from(com.concordium.grpc.v2.AccessStructure x) {
        val keys = x.getAccessPublicKeysList()
                .stream()
                .map(UpdateKeysIndex::getValue)
                .map(Index::from).collect(Collectors.toSet());
        return new AccessStructure(keys, UInt32.from(x.getAccessThreshold().getValue()));
    }
}
