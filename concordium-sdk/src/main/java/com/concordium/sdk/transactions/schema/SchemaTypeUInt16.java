package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ShortNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeUInt16 extends SchemaTypeTyped<UInt16> {
    SchemaTypeUInt16() {
        super(UInt16.class);
    }

    @Override
    UInt16 fromSchemaBytes(ByteBuffer bytes) {
        val value = bytes.order(ByteOrder.LITTLE_ENDIAN).getShort();

        return UInt16.from(value & 0xFFFF);
    }

    @Override
    byte[] getSchemaBytes(UInt16 value) {
        return ByteBuffer.allocate(UInt16.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) value.getValue())
                .array();
    }

    @Override
    JsonNode getSchemaJson(UInt16 value) {
        return ShortNode.valueOf((short) value.getValue());
    }
}
