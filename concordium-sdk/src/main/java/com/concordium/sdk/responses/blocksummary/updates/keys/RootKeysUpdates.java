package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.grpc.v2.HigherLevelKeys;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * Root update keys.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class RootKeysUpdates {
    private final List<VerificationKey> verificationKeys;
    private final int threshold;

    @JsonCreator
    RootKeysUpdates(@JsonProperty("keys") List<VerificationKey> verificationKeys,
                    @JsonProperty("threshold") int threshold) {
        this.verificationKeys = verificationKeys;
        this.threshold = threshold;
    }

    /**
     * Parses {@link HigherLevelKeys} to {@link RootKeysUpdates}.
     *
     * @param rootKeysUpdate {@link HigherLevelKeys} returned by the GRPC V2 API.
     * @return parsed {@link RootKeysUpdates}.
     */
    public static RootKeysUpdates parse(HigherLevelKeys rootKeysUpdate) {
        val keys = new ImmutableList.Builder<VerificationKey>();
        rootKeysUpdate.getKeysList().forEach(k -> keys.add(VerificationKey.parse(k)));
        return RootKeysUpdates.builder()
                .verificationKeys(keys.build())
                .threshold(rootKeysUpdate.getThreshold().getValue())
                .build();

    }
}
