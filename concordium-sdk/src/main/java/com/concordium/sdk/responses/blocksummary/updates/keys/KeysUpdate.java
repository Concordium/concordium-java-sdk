package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Root update & Level 1 update keys.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class KeysUpdate {
    @JsonProperty("keys")
    @Singular
    private final List<VerificationKey> verificationKeys;
    @JsonProperty("threshold")
    private final int threshold;
}
