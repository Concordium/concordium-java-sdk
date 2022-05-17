package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Level 2 key authorizations.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class Authorization {
    /**
     * The threshold
     */
    private final byte threshold;

    /**
     * The indices of the authorized keys.
     * See {@link Level2KeysUpdates#getVerificationKeys()} for the list of keys which these indices serve as pointers.
     */
    private final List<Integer> authorizedKeys;

    @JsonCreator
    Authorization(@JsonProperty("threshold") byte threshold, @JsonProperty("authorizedKeys") List<Integer> authorizedKeys) {
        this.threshold = threshold;
        this.authorizedKeys = authorizedKeys;
    }
}
