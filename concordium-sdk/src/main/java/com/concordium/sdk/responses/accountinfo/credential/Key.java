package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Key {
    private final String verifyKey;
    private final String schemeId;

    @JsonCreator
    Key(@JsonProperty("verifyKey") String verifyKey,
        @JsonProperty("schemeId") String schemeId) {
        this.verifyKey = verifyKey;
        this.schemeId = schemeId;
    }
}
