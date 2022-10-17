package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.ByteBuffer;

/**
 * Base interface for Types of Schema a Module can have.
 */
public interface SchemaType {

    /**
     * Converts Json value into bytes depending on {@link SchemaType}.
     *
     * @param json Json of the Value that needs to be converted to bytes.
     * @return bytes of input Json for {@link SchemaType}.
     */
    byte[] jsonToBytes(JsonNode json);

    JsonNode bytesToJson(ByteBuffer bytes);
}
