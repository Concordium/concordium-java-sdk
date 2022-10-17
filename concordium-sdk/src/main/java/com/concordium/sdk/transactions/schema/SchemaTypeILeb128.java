package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.ILeb128;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

@Getter
@Builder
@EqualsAndHashCode
class SchemaTypeILeb128 extends SchemaTypeTyped<ILeb128> {
    private final int length;

    @Builder
    SchemaTypeILeb128(final int length) {
        super(ILeb128.class);
        this.length = length;
    }

    @Override
    ILeb128 fromSchemaBytes(ByteBuffer bytes) {
        //todo: add support for ILEB128
        throw new NotImplementedException();
    }

    @Override
    byte[] getSchemaBytes(ILeb128 value) {
        //todo: add support for ILEB128
        throw new NotImplementedException();
    }

    @Override
    JsonNode getSchemaJson(ILeb128 value) {
        return BigIntegerNode.valueOf(value.getValue());
    }
}
