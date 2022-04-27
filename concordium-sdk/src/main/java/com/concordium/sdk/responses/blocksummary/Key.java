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
    private final RootKeysUpdates rootKeysUpdates;
    private final Level2KeysUpdates level2KeysUpdates;
    private final Level1KeysUpdates level1KeysUpdates;

    @JsonCreator
    Key(@JsonProperty("verifyKey") String verifyKey,
               @JsonProperty("schemeId") String schemeId,
               @JsonProperty("rootKeys") RootKeysUpdates rootKeysUpdates,
               @JsonProperty("level2Keys") Level2KeysUpdates level2KeysUpdates,
               @JsonProperty("level1Keys") Level1KeysUpdates level1KeysUpdates) {
        this.verifyKey = verifyKey;
        this.schemeId = schemeId;
        this.rootKeysUpdates = rootKeysUpdates;
        this.level2KeysUpdates = level2KeysUpdates;
        this.level1KeysUpdates = level1KeysUpdates;
    }
}
