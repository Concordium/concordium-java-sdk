package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.Index;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Credential keys (i.e. account holder keys).
 */
@Getter
@ToString
public final class CredentialPublicKeys {
    private final Map<Index, Key> keys;
    private final int threshold;

    @JsonCreator
    CredentialPublicKeys(@JsonProperty("keys") Map<Index, Key> keys,
                         @JsonProperty("threshold") int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }
}
