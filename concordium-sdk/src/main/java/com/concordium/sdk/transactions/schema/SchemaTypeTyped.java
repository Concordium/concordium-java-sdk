package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.ByteBuffer;

@EqualsAndHashCode
@Getter
abstract class SchemaTypeTyped<T> implements SchemaType {
    private final Class<T> tClass;

    protected SchemaTypeTyped(
            final Class<T> tClass) {
        this.tClass = tClass;
    }

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

    protected T fromJson(JsonNode json) throws JsonProcessingException {
        return JsonMapper.INSTANCE.treeToValue(json, this.tClass);
    }

    abstract T fromSchemaBytes(final ByteBuffer bytes);

    abstract byte[] getSchemaBytes(final T value);

    abstract JsonNode getSchemaJson(final T value);
}
