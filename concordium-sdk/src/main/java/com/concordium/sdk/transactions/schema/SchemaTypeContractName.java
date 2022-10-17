package com.concordium.sdk.transactions.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
@EqualsAndHashCode
class SchemaTypeContractName extends SchemaTypeString {

    SchemaTypeContractName(final SizeLength sizeLength) {
        super(sizeLength, StandardCharsets.US_ASCII);
    }
}
