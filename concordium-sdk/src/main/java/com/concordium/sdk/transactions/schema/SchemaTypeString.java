package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.*;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
class SchemaTypeString implements SchemaType {
    private final SizeLength length;
    private final Charset characterSet;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        try {
            return getSchemaBytes(fromJson(json));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert to Java Type", e);
        }
    }

    @Override
    public JsonNode bytesToJson(final ByteBuffer bytes) {
        return getSchemaJson(fromSchemaBytes(bytes));
    }

    protected String fromJson(JsonNode json) throws JsonProcessingException {
        return json.asText("");
    }

    String fromSchemaBytes(final ByteBuffer bytes) {
        val bytesCount = Math.toIntExact(length.getValue(bytes));
        val valueBytes = new byte[bytesCount];
        bytes.get(valueBytes);

        return new String(valueBytes, characterSet);
    }

    byte[] getSchemaBytes(final String value) {
        val strBytes = value.getBytes(characterSet);
        val sizeLengthBytes = length.getValueBytes(strBytes.length);

        return ByteBuffer.allocate(sizeLengthBytes.length + strBytes.length)
                .put(sizeLengthBytes)
                .put(strBytes)
                .array();
    }

    JsonNode getSchemaJson(final String value) {
        return TextNode.valueOf(value);
    }
}
