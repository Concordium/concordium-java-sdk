package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Bool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeBool extends SchemaTypeTyped<Bool> {
    SchemaTypeBool() {
        super(Bool.class);
    }

    @Override
    Bool fromSchemaBytes(ByteBuffer bytes) {
        val byteValue = bytes.get();

        switch (byteValue) {
            case 0:
                return Bool.from(false);
            case 1:
                return Bool.from(true);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    byte[] getSchemaBytes(Bool value) {
        return new byte[value.getValue() ? 1 : 0];
    }

    @Override
    JsonNode getSchemaJson(Bool value) {
        return BooleanNode.valueOf(value.getValue());
    }
}
