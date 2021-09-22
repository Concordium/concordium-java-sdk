package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.Getter;
import lombok.ToString;

@Getter
public class GTUAmount {
    private final UInt64 value;

    private GTUAmount(UInt64 value) {
        this.value = value;
    }

    public static GTUAmount fromMicro(long val) {
        return new GTUAmount(UInt64.from(val));
    }

    public static GTUAmount fromMicro(String val) {
        return new GTUAmount(UInt64.from(val));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
