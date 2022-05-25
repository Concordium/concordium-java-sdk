package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Level 1 update keys
 */
@Getter
@ToString
public final class Level1KeysUpdates {
    private final List<VerificationKey> verificationKeys;
    private final int threshold;

    @JsonCreator
    Level1KeysUpdates(@JsonProperty("keys") List<VerificationKey> verificationKeys,
                      @JsonProperty("threshold") int threshold){
        this.verificationKeys = verificationKeys;
        this.threshold = threshold;
    }
}
