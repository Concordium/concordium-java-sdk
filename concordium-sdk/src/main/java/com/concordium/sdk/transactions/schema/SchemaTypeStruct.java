package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
class SchemaTypeStruct implements SchemaType {
    private final SchemaTypeFields fields;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        return fields.jsonToBytes(json);
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        return fields.bytesToJson(bytes);
    }
}
