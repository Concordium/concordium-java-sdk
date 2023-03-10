package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.Index;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lombok.*;

import java.util.Map;

/**
 * Public keys of the {@link Credential}.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CredentialPublicKeys {
    @Singular
    private final ImmutableMap<Index, Key> keys;
    private final int threshold;

    @JsonCreator
    CredentialPublicKeys(@JsonProperty("keys") Map<Index, Key> keys,
                         @JsonProperty("threshold") int threshold) {
        this.keys = ImmutableMap.copyOf(keys);
        this.threshold = threshold;
    }
}
