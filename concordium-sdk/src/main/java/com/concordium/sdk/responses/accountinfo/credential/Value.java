package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Value {
    private final Contents contents;
    /**
     * The type of the {@link Credential}
     */
    private final CredentialType type;

    @JsonCreator
    public Value(@JsonProperty("contents") Contents contents,
                 @JsonProperty("type") CredentialType type) {
        this.contents = contents;
        this.type = type;
    }
}
