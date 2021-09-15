package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Value {
    private final Contents contents;
    private final String type;

    @JsonCreator
    public Value(@JsonProperty("contents") Contents contents,
                 @JsonProperty("type") String type) {
        this.contents = contents;
        this.type = type;
    }
}
