package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt128;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.EqualsAndHashCode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeUInt128 extends SchemaTypeTyped<UInt128> {
    SchemaTypeUInt128() {
        super(UInt128.class);
    }

    @Override
    UInt128 fromSchemaBytes(ByteBuffer bytes) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    byte[] getSchemaBytes(UInt128 value) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    JsonNode getSchemaJson(UInt128 value) {
        return BigIntegerNode.valueOf(value.getValue());
    }
}
