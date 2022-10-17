package com.concordium.sdk.types;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class Tuple<TLeft, TRight> {

    private final TLeft left;
    private final TRight right;

    @JsonValue
    public Object[] toJson() {
        return new Object[]{left, right};
    }
}
