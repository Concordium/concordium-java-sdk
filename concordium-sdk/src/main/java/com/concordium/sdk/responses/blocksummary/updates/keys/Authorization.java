package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Level 2 key authorizations.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Jacksonized
public final class Authorization {
    /**
     * The threshold
     */
    @JsonProperty("threshold")
    private final byte threshold;

    /**
     * The indices of the authorized keys.
     * See {@link Level2KeysUpdates#getVerificationKeys()} for the list of keys which these indices serve as pointers.
     */
    @JsonProperty("authorizedKeys")
    @Singular
    private final List<Integer> authorizedKeys;
}
