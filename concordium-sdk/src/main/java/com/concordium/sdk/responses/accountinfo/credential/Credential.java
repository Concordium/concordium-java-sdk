package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * A credential on the chain.
 */
@Getter
@ToString
public final class Credential {
    /**
     * The version.
     */
    private final String version;
    private final Value value;

    @JsonCreator
    Credential(@JsonProperty("v") String version,
               @JsonProperty("value") Value value) {
        this.version = version;
        this.value = value;
    }
}
