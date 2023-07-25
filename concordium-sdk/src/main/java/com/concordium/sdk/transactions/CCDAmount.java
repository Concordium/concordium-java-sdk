package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

/**
 * A CCD amount with 'micro' precision.
 */
@Getter
@EqualsAndHashCode
public class CCDAmount {
    public static final int BYTES = UInt64.BYTES;
    private final UInt64 value;

    private CCDAmount(UInt64 value) {
        this.value = value;
    }

    public static CCDAmount fromMicro(long val) {
        return new CCDAmount(UInt64.from(val));
    }

    public static CCDAmount fromMicro(String val) {
        return new CCDAmount(UInt64.from(val));
    }

    @JsonCreator
    CCDAmount(String amount) {
        this.value = UInt64.from(amount);
    }

    byte[] getBytes() {
        return value.getBytes();
    }

    public static CCDAmount fromBytes(ByteBuffer source) {
        UInt64 value = UInt64.fromBytes(source);
        return new CCDAmount(value);
    }

    @Override
    public String toString() {
        return (DecimalFormat.getNumberInstance().format(value.getValue()/ 1000000) + " CCD");
    }

    @JsonValue
    public String toJsonString() {
        return String.valueOf(this.value.getValue());
    }
}
