package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeUnit implements SchemaType {
    @Override
    public byte[] jsonToBytes(JsonNode json) {
        return new byte[0];
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        return NullNode.instance;
    }
}
