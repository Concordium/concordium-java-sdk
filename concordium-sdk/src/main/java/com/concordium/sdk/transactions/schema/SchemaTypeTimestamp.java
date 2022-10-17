package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeTimestamp extends SchemaTypeTyped<Timestamp> {
    private final SchemaTypeUInt64 uInt64 = new SchemaTypeUInt64();

    SchemaTypeTimestamp() {
        super(Timestamp.class);
    }

    @Override
    Timestamp fromSchemaBytes(ByteBuffer bytes) {
        return Timestamp.newMillis(uInt64.fromSchemaBytes(bytes).getValue());
    }

    @Override
    byte[] getSchemaBytes(Timestamp value) {
        return uInt64.getSchemaBytes(UInt64.from(value.getMillis()));
    }

    @Override
    JsonNode getSchemaJson(Timestamp value) {
        return uInt64.getSchemaJson(UInt64.from(value.getMillis()));
    }
}
