package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public class Duration {

    private final UInt64 value;

    Duration(UInt64 value) {
        this.value = value;
    }

    @JsonCreator
    public static Duration from(String millis) {
        return new Duration(UInt64.from(millis));
    }

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }

    public static Duration from(long millis) {
        return new Duration(UInt64.from(millis));
    }

    public static Duration from(UInt64 millis) {
        return new Duration(millis);
    }
}
