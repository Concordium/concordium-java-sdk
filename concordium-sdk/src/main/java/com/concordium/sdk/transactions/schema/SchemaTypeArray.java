package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
class SchemaTypeArray extends SchemaTypeList {
    private final int length;

    SchemaTypeArray(final int length, final SchemaType type) {
        super(SizeLength.from(2), type);
        this.length = length;
    }
}
