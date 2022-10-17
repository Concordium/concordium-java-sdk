package com.concordium.sdk.transactions.schema;

import lombok.*;

@Builder
@Data
class SchemaTypeEnumVariant {
    private final String name;
    private final SchemaTypeFields fields;
}
