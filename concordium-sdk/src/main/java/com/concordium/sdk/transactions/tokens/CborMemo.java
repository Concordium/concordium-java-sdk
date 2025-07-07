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

    /**
     * Memo content to be CBOR-encoded (Jackson), up to 256 bytes total
     */
    private final byte[] content;

    public CborMemo(byte[] content) {
        if (content.length > 256) {
            throw new IllegalArgumentException("The content can't exceed 256 bytes");
        }
        this.content = content;
    }

    public CborMemo(Object content) throws JsonProcessingException {
        this(CborMapper.INSTANCE.writeValueAsBytes(content));
    }

    static class CborSerializer extends JsonSerializer<CborMemo> {

        @Override
        public void serialize(CborMemo cborMemo,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;

            cborGenerator.writeTag(24);

            if (cborMemo == null) {
                cborGenerator.writeNull();
                return;
            }

            cborGenerator.writeObject(cborMemo.content);
        }
    }
}
