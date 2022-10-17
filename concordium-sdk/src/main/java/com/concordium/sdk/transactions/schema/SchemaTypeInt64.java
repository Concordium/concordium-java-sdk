package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Int64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeInt64 extends SchemaTypeTyped<Int64> {
    SchemaTypeInt64() {
        super(Int64.class);
    }

    @Override
    Int64 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getLong();
        return Int64.from(value);
    }

    @Override
    byte[] getSchemaBytes(Int64 value) {
        return ByteBuffer.allocate(Int64.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putLong(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(Int64 value) {
        return LongNode.valueOf(value.getValue());
    }
}
