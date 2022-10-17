package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Int128;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.EqualsAndHashCode;
import lombok.val;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode
class SchemaTypeInt128 extends SchemaTypeTyped<Int128> {
    SchemaTypeInt128() {
        super(Int128.class);
    }

    @Override
    Int128 fromSchemaBytes(ByteBuffer bytes) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    byte[] getSchemaBytes(Int128 value) {
        //todo: implement this
        throw new NotImplementedException();
    }

    @Override
    JsonNode getSchemaJson(Int128 value) {
        return BigIntegerNode.valueOf(value.getValue());
    }
}
