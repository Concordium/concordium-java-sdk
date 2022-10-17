package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
class SchemaTypeTuple implements SchemaType {
    private final SchemaType left;
    private final SchemaType right;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        if (json.isArray()) {
            return jsonArrayToBytes((ArrayNode) json);
        } else if (json.isObject()) {
            return jsonObjectToBytes((ObjectNode) json);
        } else {
            throw new RuntimeException("should be an array or object");
        }
    }

    private byte[] jsonObjectToBytes(ObjectNode json) {
        val left = this.left.jsonToBytes(json.get("left"));
        val right = this.right.jsonToBytes(json.get("right"));
        return toBytes(left, right);
    }

    private byte[] jsonArrayToBytes(ArrayNode json) {
        val left = this.left.jsonToBytes(json.get(0));
        val right = this.right.jsonToBytes(json.get(1));
        return toBytes(left, right);
    }

    private byte[] toBytes(byte[] left, byte[] right) {
        val ret = ByteBuffer.allocate(left.length + right.length);
        ret.put(left);
        ret.put(right);

        return ret.array();
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        val leftJson = left.bytesToJson(bytes);
        val rightJson = right.bytesToJson(bytes);
        val ret = JsonMapper.INSTANCE.createObjectNode();
        ret.put("left", leftJson);
        ret.put("right", rightJson);

        return ret;
    }
}
