package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
class SchemaTypeMap extends SchemaTypeList {
    SchemaTypeMap(final SizeLength sizeLength, final SchemaTypeTuple entryType) {
        super(sizeLength, entryType);
    }
}
