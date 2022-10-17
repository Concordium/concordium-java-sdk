package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeUInt64 extends SchemaTypeTyped<UInt64> {
    SchemaTypeUInt64() {
        super(UInt64.class);
    }

    @Override
    UInt64 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getLong();
        return UInt64.from(value);
    }

    @Override
    byte[] getSchemaBytes(UInt64 value) {
        return ByteBuffer.allocate(UInt64.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putLong(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(UInt64 value) {
        return LongNode.valueOf(value.getValue());
    }
}
