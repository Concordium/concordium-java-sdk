package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

@Getter
@JsonSerialize(using = CborMemo.CborSerializer.class)
public class CborMemo {

    private final byte[] data;

    /**
     * @param content memo content to be CBOR-encoded (Jackson), up to 256 bytes total
     * @throws JsonProcessingException if the content can't be encoded
     */
    public CborMemo(Object content) throws JsonProcessingException {
        val data = CborMapper.INSTANCE.writeValueAsBytes(content);
        if (data.length > 256) {
            throw new IllegalArgumentException("Encoded content can't exceed 256 bytes");
        }

        this.data = data;
    }

    static class CborSerializer extends JsonSerializer<CborMemo> {

        @Override
        public void serialize(CborMemo cborMemo,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;
            cborGenerator.writeTag(24);
            cborGenerator.writeBytes(cborMemo.data, 0, cborMemo.data.length);
        }
    }
}
