package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class Bool {
    private final boolean value;

    Bool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    public static Bool from(boolean value) {
        return new Bool(value);
    }

    @JsonCreator
    public static Bool fromJson(String json) {
        return new Bool(json.toLowerCase() == "true");
    }
}
