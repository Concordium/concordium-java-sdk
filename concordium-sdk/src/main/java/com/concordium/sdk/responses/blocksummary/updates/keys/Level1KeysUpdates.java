package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.concordium.grpc.v2.HigherLevelKeys;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * Level 1 update keys
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class Level1KeysUpdates {
    private final List<VerificationKey> verificationKeys;
    private final int threshold;

    @JsonCreator
    Level1KeysUpdates(@JsonProperty("keys") List<VerificationKey> verificationKeys,
                      @JsonProperty("threshold") int threshold) {
        this.verificationKeys = verificationKeys;
        this.threshold = threshold;
    }

    /**
     * Parses {@link HigherLevelKeys} to {@link Level1KeysUpdates}.
     *
     * @param level1KeysUpdate {@link HigherLevelKeys} returned by the GRPC V2 API.
     * @return parsed {@link Level1KeysUpdates}.
     */
    public static Level1KeysUpdates parse(HigherLevelKeys level1KeysUpdate) {
        val keys = new ImmutableList.Builder<VerificationKey>();
        level1KeysUpdate.getKeysList().forEach(k -> keys.add(VerificationKey.parse(k)));
        return Level1KeysUpdates.builder()
                .verificationKeys(keys.build())
                .threshold(level1KeysUpdate.getThreshold().getValue())
                .build();
    }
}

