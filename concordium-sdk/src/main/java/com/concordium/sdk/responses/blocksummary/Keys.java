package com.concordium.sdk.responses.blocksummary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Keys {
    private final RootKeysUpdates rootKeysUpdates;
    private final Level1KeysUpdates level1KeysUpdates;
    private final Level2KeysUpdates level2KeysUpdates;

    @JsonCreator
    Keys(@JsonProperty("rootKeys") RootKeysUpdates rootKeysUpdates,
                @JsonProperty("level1Keys") Level1KeysUpdates level1KeysUpdates,
                @JsonProperty("level2Keys") Level2KeysUpdates level2KeysUpdates) {
        this.rootKeysUpdates = rootKeysUpdates;
        this.level1KeysUpdates = level1KeysUpdates;
        this.level2KeysUpdates = level2KeysUpdates;
    }
}
