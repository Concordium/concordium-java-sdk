package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeDuration extends SchemaTypeTyped<Duration> {

    private final SchemaTypeUInt64 uInt64 = new SchemaTypeUInt64();

    SchemaTypeDuration() {
        super(Duration.class);
    }

    @Override
    Duration fromSchemaBytes(ByteBuffer bytes) {
        return Duration.from(uInt64.fromSchemaBytes(bytes));
    }

    @Override
    byte[] getSchemaBytes(Duration value) {
        return uInt64.getSchemaBytes(value.getValue());
    }

    @Override
    JsonNode getSchemaJson(Duration value) {
        return uInt64.getSchemaJson(value.getValue());
    }
}
