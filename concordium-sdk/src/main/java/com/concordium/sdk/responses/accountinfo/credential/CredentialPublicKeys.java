package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public final class CredentialPublicKeys {
    private final Map<String, Key> keys;
    private final int threshold;

    @JsonCreator
    CredentialPublicKeys(@JsonProperty("keys") Map<String, Key> keys,
                         @JsonProperty("threshold") int threshold) {
        this.keys = keys;
        this.threshold = threshold;
    }
}
