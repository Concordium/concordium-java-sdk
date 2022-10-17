package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Int8;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeInt8 extends SchemaTypeTyped<Int8> {

    SchemaTypeInt8() {
        super(Int8.class);
    }

    @Override
    Int8 fromSchemaBytes(ByteBuffer bytes) {
        return Int8.from(bytes.get());
    }

    @Override
    byte[] getSchemaBytes(Int8 value) {
        return ByteBuffer.allocate(Int8.BYTES).put(new byte[]{value.getValue()}).array();
    }

    @Override
    JsonNode getSchemaJson(Int8 value) {
        return IntNode.valueOf(value.getValue());
    }
}
