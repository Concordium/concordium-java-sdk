package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Authorizations for updating chain parameters.
 * Root keys can change root and level 1 keys.
 * Level one keys can change level 1 and 2 keys.
 * Level 2 keys are the actual keys used updating chain parameters.
 */
@Getter
@ToString
@Builder
@Jacksonized
public final class Keys {
    @JsonProperty("rootKeys")
    private final KeysUpdate rootKeysUpdates;
    @JsonProperty("level1Keys")
    private final KeysUpdate level1KeysUpdates;
    @JsonProperty("level2Keys")
    private final Level2KeysUpdates level2KeysUpdates;
}
