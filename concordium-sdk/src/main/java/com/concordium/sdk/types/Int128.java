package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(staticName = "from")
public class Int128 {
    public static final int BYTES = 16;
    private final BigInteger value;

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }

    @JsonCreator
    public static BigInteger from(String value) {
        return new BigInteger(value);
    }
}
