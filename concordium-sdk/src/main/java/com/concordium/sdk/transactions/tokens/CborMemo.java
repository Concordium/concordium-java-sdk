package com.concordium.sdk.transactions.tokens;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

@Data
@Getter
@JsonSerialize(using = CborMemo.CborSerializer.class)
public class CborMemo {

    /**
     * Memo content to be CBOR-encoded (Jackson), up to 256 bytes total
     */
    private final Object content;

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
