package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.ContractAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeContractAddress extends SchemaTypeTyped<ContractAddress> {
    private final SchemaTypeUInt64 uInt64;

    SchemaTypeContractAddress() {
        super(ContractAddress.class);
        this.uInt64 = new SchemaTypeUInt64();
    }

    @Override
    ContractAddress fromSchemaBytes(ByteBuffer bytes) {
        return new ContractAddress(uInt64.fromSchemaBytes(bytes), uInt64.fromSchemaBytes(bytes));
    }

    @Override
    byte[] getSchemaBytes(ContractAddress value) {
        return ByteBuffer.allocate(UInt64.BYTES * 2)
                .put(uInt64.getSchemaBytes(value.getIndex()))
                .put(uInt64.getSchemaBytes(value.getSubindex()))
                .array();
    }

    @Override
    JsonNode getSchemaJson(ContractAddress value) {
        val json = JsonMapper.INSTANCE.createObjectNode();
        json.put("index", value.getIndex().getValue());
        json.put("subindex", value.getSubindex().getValue());

        return json;
    }
}
