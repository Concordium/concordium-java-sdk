package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeFieldsNone implements SchemaTypeFields {
    @Override
    public byte[] jsonToBytes(JsonNode json) {
        return new byte[0];
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        return JsonMapper.INSTANCE.createArrayNode();
    }
}
