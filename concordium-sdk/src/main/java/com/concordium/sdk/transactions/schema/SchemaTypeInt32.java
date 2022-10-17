package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Int32;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeInt32 extends SchemaTypeTyped<Int32> {
    SchemaTypeInt32() {
        super(Int32.class);
    }

    @Override
    Int32 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getInt();

        return Int32.from(value);
    }

    @Override
    byte[] getSchemaBytes(Int32 value) {
        return ByteBuffer.allocate(Int32.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(Int32 value) {
        return IntNode.valueOf(value.getValue());
    }
}
