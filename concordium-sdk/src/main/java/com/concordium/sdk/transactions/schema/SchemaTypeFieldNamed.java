package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class SchemaTypeFieldNamed {
    private final String name;
    private final SchemaType type;
}
