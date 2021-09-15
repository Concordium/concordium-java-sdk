package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Credential {
    private final String version;
    private final Value value;

    @JsonCreator
    Credential(@JsonProperty("v") String version,
               @JsonProperty("value") Value value) {
        this.version = version;
        this.value = value;
    }
}
