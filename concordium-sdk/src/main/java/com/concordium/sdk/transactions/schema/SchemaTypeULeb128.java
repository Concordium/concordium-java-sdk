package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.ULeb128;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
class SchemaTypeULeb128 extends SchemaTypeTyped<ULeb128> {
    private final int length;

    SchemaTypeULeb128(final int length) {
        super(ULeb128.class);
        this.length = length;
    }

    @Override
    ULeb128 fromSchemaBytes(ByteBuffer bytes) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    byte[] getSchemaBytes(ULeb128 value) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    JsonNode getSchemaJson(ULeb128 value) {
        return BigIntegerNode.valueOf(value.getValue());
    }
}
