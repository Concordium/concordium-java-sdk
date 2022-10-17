package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
class SchemaTypeList implements SchemaType {
    private final SizeLength sizeLength;
    private final SchemaType type;

    public byte[] jsonToBytes(JsonNode json) {
        val arrayList = new ArrayList<byte[]>();
        int totalBytesLength = 0;

        if (!json.isArray()) {
            throw new RuntimeException("Should be an array");
        }

        val itr = json.iterator();
        while (itr.hasNext()) {
            val bytes = type.jsonToBytes(itr.next());
            totalBytesLength += bytes.length;
            arrayList.add(bytes);
        }

        val sizeLengthBytes = sizeLength.getValueBytes(arrayList.size());
        val ret = ByteBuffer
                .allocate(sizeLengthBytes.length + totalBytesLength)
                .put(sizeLengthBytes);

        for (int i = 0; i < arrayList.size(); i++) {
            ret.put(arrayList.get(i));
        }

        return ret.array();
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer buff) {
        val length = this.sizeLength.getValue(buff);
        val ret = JsonMapper.INSTANCE.createArrayNode();

        for (int i = 0; i < length; i++) {
            ret.add(type.bytesToJson(buff));
        }

        return ret;
    }
}
