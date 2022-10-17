package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
class SchemaTypeByteArray extends SchemaTypeByteList {
    private final int length;

    SchemaTypeByteArray(int length) {
        super(SizeLength.from(2));
        this.length = length;
    }
}
