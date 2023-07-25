package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.UpdatePublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.google.protobuf.ByteString;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents root or level 1 keys.
 */
@ToString
@EqualsAndHashCode
@Getter
@Builder
public class HigherLevelKeys {
    /**
     * The keys that are allowed to perform root key or level 1 key updates given by
     * the context.
     */
    private final Set<ED25519PublicKey> publicKeys;

    /**
     * Threshold required for performing the update.
     */
    private final int threshold;

    public HigherLevelKeys(Set<ED25519PublicKey> publicKeys, int threshold) {
        this.publicKeys = publicKeys;
        this.threshold = threshold;
    }

    public static HigherLevelKeys from(com.concordium.grpc.v2.HigherLevelKeys value) {
        val keys = value.getKeysList()
                .stream()
                .map(UpdatePublicKey::getValue)
                .map(ByteString::toByteArray)
                .map(ED25519PublicKey::from)
                .collect(Collectors.toSet());
        return new HigherLevelKeys(keys, value.getThreshold().getValue());
    }
}
