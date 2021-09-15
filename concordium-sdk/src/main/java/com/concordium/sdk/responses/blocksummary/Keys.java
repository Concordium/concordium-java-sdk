package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Keys {
    private final RootKeys rootKeys;
    private final Level1Keys level1Keys;
    private final Level2Keys level2Keys;

    @JsonCreator
    Keys(@JsonProperty("rootKeys") RootKeys rootKeys,
                @JsonProperty("level1Keys") Level1Keys level1Keys,
                @JsonProperty("level2Keys") Level2Keys level2Keys) {
        this.rootKeys = rootKeys;
        this.level1Keys = level1Keys;
        this.level2Keys = level2Keys;
    }
}
