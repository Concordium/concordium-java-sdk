package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class Int8 {
    public static final int BYTES = 1;

    private final byte value;

    @JsonCreator
    public static Int8 from(String value) {
        return new Int8(Byte.parseByte(value));
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
