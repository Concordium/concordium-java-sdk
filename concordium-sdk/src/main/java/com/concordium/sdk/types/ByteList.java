package com.concordium.sdk.types;

import com.concordium.sdk.transactions.schema.ContractSchemaType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;

@EqualsAndHashCode
public class ByteList extends ArrayList<Byte> implements ContractSchemaType {

    public ByteList(byte[] bytes) {
        super(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            add(bytes[i]);
        }
    }

    @JsonCreator
    public ByteList(String hexEncodedBytes) throws DecoderException {
        this(Hex.decodeHex(hexEncodedBytes));
    }

    @Override
    public byte[] getSchemaBytes() {
        val ret = new byte[size()];

        for (int i = 0; i < size(); i++) {
            ret[i] = get(i);
        }

        return ret;
    }

    @Override
    public JsonNode getSchemaJson() {
        return TextNode.valueOf(this.toString());
    }

    @Override
    @JsonValue
    public String toString() {
        return Hex.encodeHexString(this.getSchemaBytes());
    }
}
