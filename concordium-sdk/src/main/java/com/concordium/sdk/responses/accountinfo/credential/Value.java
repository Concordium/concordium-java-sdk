package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Exists only for annotation driven deserializing.
 * See {@link Credential}
 */
@Getter
@ToString
final class Value {
    /**
     * Credential
     */
    private final Contents contents;
    /**
     * Type of the {@link Credential}
     */
    private final CredentialType type;

    @JsonCreator
    public Value(@JsonProperty("contents") Contents contents,
                 @JsonProperty("type") CredentialType type) {
        this.contents = contents;
        this.type = type;
    }
}
