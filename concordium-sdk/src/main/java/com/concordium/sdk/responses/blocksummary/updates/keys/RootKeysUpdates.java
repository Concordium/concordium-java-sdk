package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Root update keys.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class RootKeysUpdates {
    private final List<VerificationKey> verificationKeys;
    private final int threshold;

    @JsonCreator
    RootKeysUpdates(@JsonProperty("keys") List<VerificationKey> verificationKeys,
                    @JsonProperty("threshold") int threshold) {
        this.verificationKeys = verificationKeys;
        this.threshold = threshold;
    }
}
