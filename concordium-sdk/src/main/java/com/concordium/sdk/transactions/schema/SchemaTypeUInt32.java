package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeUInt32 extends SchemaTypeTyped<UInt32> {

    SchemaTypeUInt32() {
        super(UInt32.class);
    }

    @Override
    UInt32 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getInt();

        return UInt32.from(value & 0xFFFFF);
    }

    @Override
    byte[] getSchemaBytes(UInt32 value) {
        return ByteBuffer.allocate(UInt32.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(UInt32 value) {
        return IntNode.valueOf(value.getValue());
    }
}
