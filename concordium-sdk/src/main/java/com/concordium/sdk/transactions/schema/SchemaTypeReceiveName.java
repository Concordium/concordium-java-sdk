package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;

@EqualsAndHashCode
class SchemaTypeReceiveName extends SchemaTypeString {

    SchemaTypeReceiveName(final SizeLength sizeLength) {
        super(sizeLength, StandardCharsets.US_ASCII);
    }
}
