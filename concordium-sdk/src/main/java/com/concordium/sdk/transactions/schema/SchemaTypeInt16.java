package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Int16;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeInt16 extends SchemaTypeTyped<Int16> {

    SchemaTypeInt16() {
        super(Int16.class);
    }

    @Override
    Int16 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getShort();

        return Int16.from(value);
    }

    @Override
    byte[] getSchemaBytes(Int16 value) {
        return ByteBuffer.allocate(Int16.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(Int16 value) {
        return ShortNode.valueOf(value.getValue());
    }
}
