package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContractSchemaType {
    byte[] getSchemaBytes();

    JsonNode getSchemaJson();
}
