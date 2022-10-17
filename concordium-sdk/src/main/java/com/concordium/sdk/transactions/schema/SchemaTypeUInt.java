package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeUInt extends SchemaTypeTyped<UInt> {
    SchemaTypeUInt() {
        super(UInt.class);
    }

    @Override
    UInt fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).get();

        return UInt.from(value & 0xFF);
    }

    @Override
    byte[] getSchemaBytes(UInt value) {
        return ByteBuffer.allocate(UInt.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(UInt value) {
        return IntNode.valueOf(value.getValue());
    }
}
