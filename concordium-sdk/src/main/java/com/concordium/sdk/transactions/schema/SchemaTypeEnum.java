package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.Objects;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
class SchemaTypeEnum implements SchemaType {

    private final SchemaTypeUInt uInt = new SchemaTypeUInt();
    private final SchemaTypeUInt32 uInt32 = new SchemaTypeUInt32();

    @Singular
    private final ImmutableList<SchemaTypeEnumVariant> variants;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        for (val variant : variants) {
            val var = json.get(variant.getName());
            if (Objects.isNull(var)) {
                continue;
            }

            return variant.getFields().jsonToBytes(var);
        }

        throw new RuntimeException("no variant matched");
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        val idx = variants.size() <= UInt.MAX_VALUE
                ? uInt.fromSchemaBytes(bytes).getValue()
                : uInt32.fromSchemaBytes(bytes).getValue();
        val variant = variants.get(idx);
        val ret = JsonMapper.INSTANCE.createObjectNode();
        ret.put(variant.getName(), variant.getFields().bytesToJson(bytes));

        return ret;
    }
}
