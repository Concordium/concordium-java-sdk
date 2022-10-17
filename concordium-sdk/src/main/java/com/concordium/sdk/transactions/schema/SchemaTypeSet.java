package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
class SchemaTypeSet extends SchemaTypeList {
    SchemaTypeSet(SizeLength sizeLength, SchemaType type) {
        super(sizeLength, type);
    }
}
