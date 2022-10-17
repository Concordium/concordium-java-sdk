package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
class SchemaTypeFieldsNamed implements SchemaTypeFields {

    @Singular
    private final ImmutableList<SchemaTypeFieldNamed> fields;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        if (!json.isObject()) {
            throw new RuntimeException("should have been an object");
        }

        val paramsCombined = new ArrayList<byte[]>();
        int totalBytesCount = 0;
        for (val field : fields) {
            val f = json.get(field.getName());
            val bytes = field.getType().jsonToBytes(f);
            paramsCombined.add(bytes);
            totalBytesCount += bytes.length;
        }

        val ret = ByteBuffer.wrap(new byte[totalBytesCount]);
        for (byte[] arr : paramsCombined) {
            ret.put(arr);
        }

        return ret.array();
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        val ret = JsonMapper.INSTANCE.createObjectNode();

        for (val field : fields) {
            ret.put(field.getName(), field.getType().bytesToJson(bytes));
        }

        return ret;
    }
}
