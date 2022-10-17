package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class ILeb128 {
    private final BigInteger value;

    @Override
    @JsonValue
    public String toString() {
        return value.toString();
    }

    @JsonCreator
    public static ILeb128 from(String str) {
        return new ILeb128(new BigInteger(str));
    }
}
