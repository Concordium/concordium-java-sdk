package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.EqualsAndHashCode;

import java.nio.ByteBuffer;

@EqualsAndHashCode
class SchemaTypeAccountAddress extends SchemaTypeTyped<AccountAddress> {
    SchemaTypeAccountAddress() {
        super(AccountAddress.class);
    }

    @Override
    AccountAddress fromSchemaBytes(ByteBuffer bytes) {
        return AccountAddress.fromBytes(bytes);
    }

    @Override
    byte[] getSchemaBytes(AccountAddress value) {
        return ByteBuffer.allocate(AccountAddress.BYTES + 1)
                .put((byte) 0)
                .put(value.getBytes())
                .array();
    }

    @Override
    JsonNode getSchemaJson(AccountAddress value) {
        return TextNode.valueOf(value.toString());
    }
}
