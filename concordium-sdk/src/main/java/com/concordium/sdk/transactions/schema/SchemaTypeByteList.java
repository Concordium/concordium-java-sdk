package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
class SchemaTypeByteList implements SchemaType {
    private final SizeLength sizeLength;

    @Override
    public byte[] jsonToBytes(JsonNode json) {
        try {
            val bytes = Hex.decodeHex(json.textValue());
            val sizeLengthBytes = sizeLength.getValueBytes(bytes.length);

            return ByteBuffer.allocate(sizeLengthBytes.length + bytes.length)
                    .put(sizeLengthBytes)
                    .put(bytes)
                    .array();
        } catch (DecoderException e) {
            throw new RuntimeException("could not read SchemaTypeByteList from json", e);
        }
    }

    @Override
    public JsonNode bytesToJson(ByteBuffer bytes) {
        val sizeLength = this.sizeLength.getValue(bytes);
        val valueBytes = new byte[Math.toIntExact(sizeLength)];
        bytes.get(valueBytes);

        return TextNode.valueOf(Hex.encodeHexString(valueBytes));
    }
}
