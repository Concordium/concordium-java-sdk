package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeAmount extends SchemaTypeTyped<CCDAmount> {
    private final SchemaTypeUInt64 uInt64;

    SchemaTypeAmount() {
        super(CCDAmount.class);
        this.uInt64 = new SchemaTypeUInt64();
    }

    @Override
    CCDAmount fromSchemaBytes(ByteBuffer bytes) {
        return CCDAmount.fromMicro(uInt64.fromSchemaBytes(bytes));
    }

    @Override
    byte[] getSchemaBytes(CCDAmount value) {
        return uInt64.getSchemaBytes(value.getValue());
    }

    @Override
    JsonNode getSchemaJson(CCDAmount value) {
        return uInt64.getSchemaJson(value.getValue());
    }
}
