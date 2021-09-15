package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Key {
    private final String verifyKey;
    private final String schemeId;
    private final RootKeys rootKeys;
    private final Level2Keys level2Keys;
    private final Level1Keys level1Keys;

    @JsonCreator
    Key(@JsonProperty("verifyKey") String verifyKey,
               @JsonProperty("schemeId") String schemeId,
               @JsonProperty("rootKeys") RootKeys rootKeys,
               @JsonProperty("level2Keys") Level2Keys level2Keys,
               @JsonProperty("level1Keys") Level1Keys level1Keys) {
        this.verifyKey = verifyKey;
        this.schemeId = schemeId;
        this.rootKeys = rootKeys;
        this.level2Keys = level2Keys;
        this.level1Keys = level1Keys;
    }
}
