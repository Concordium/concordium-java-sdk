package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UInt128 {
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

    public static UInt128 from(BigInteger value) {
        if (value.signum() == -1) {
            throw new RuntimeException("value should not be negative");
        }

        return new UInt128(value);
    }
}
