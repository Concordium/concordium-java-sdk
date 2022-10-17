package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
class SchemaTypeFieldsUnNamed implements SchemaTypeFields {

    @Singular
    private final ImmutableList<SchemaType> fields;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        try {
            val paramsCombined = new ArrayList<byte[]>();
            int totalLength = 0;

            for (int i = 0; i < fields.size(); i++) {
                val field = fields.get(i);
                val node = json.get(i);
                val params = field.jsonToBytes(node);
                paramsCombined.add(params);
                totalLength += params.length;
            }

            val ret = ByteBuffer.wrap(new byte[totalLength]);
            for (byte[] arr : paramsCombined) {
                ret.put(arr);
            }

            return ret.array();
        } catch (Exception ex) {
            throw new RuntimeException("Could not parse SchemaTypeFieldsUnNamed", ex);
        }
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        val ret = JsonMapper.INSTANCE.createArrayNode();

        for (val field : fields) {
            ret.add(field.bytesToJson(bytes));
        }

        return ret;
    }
}
