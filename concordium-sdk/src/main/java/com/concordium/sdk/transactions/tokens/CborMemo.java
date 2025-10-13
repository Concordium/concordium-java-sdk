package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.serializing.CborMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Getter
@JsonSerialize(using = CborMemo.CborSerializer.class)
@JsonDeserialize(using = CborMemo.CborDeserializer.class)
public class CborMemo {

    /**
     * CBOR-encoded content, up to 256 bytes total.
     */
    private final byte[] content;

    private CborMemo(byte[] content) {
        if (content.length > 256) {
            throw new IllegalArgumentException("The content can't exceed 256 bytes");
        }
        this.content = content;
    }

    /**
     * @param content which is already CBOR-encoded, up to 256 bytes.
     */
    public static CborMemo from(byte[] content) {
        return new CborMemo(content);
    }

    /**
     * @param content to be CBOR-encoded (Jackson), up to 256 bytes total.
     */
    @SneakyThrows
    public static CborMemo from(Object content) {
        return new CborMemo(CborMapper.INSTANCE.writeValueAsBytes(content));
    }

    @Override
    public String toString() {
        return "CborMemo(" +
                "encodedContent=" + Hex.encodeHexString(content) +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CborMemo)) return false;
        CborMemo cborMemo = (CborMemo) o;
        return Objects.deepEquals(content, cborMemo.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    static class CborSerializer extends JsonSerializer<CborMemo> {

        @Override
        public void serialize(CborMemo cborMemo,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;
            cborGenerator.writeTag(CBOR_TAG);
            cborGenerator.writeObject(cborMemo.content);
        }
    }

    static class CborDeserializer extends JsonDeserializer<CborMemo> {

        @Override
        public CborMemo deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws IOException {
            val cborParser = (CBORParser) jsonParser;
            val currentTag = cborParser.getCurrentTag();
            if (currentTag != CBOR_TAG) {
                throw new JsonParseException(
                        jsonParser,
                        "Expected CBOR memo tag " + CBOR_TAG + ", but read " + currentTag
                );
            }
            return new CborMemo(cborParser.getBinaryValue());
        }
    }

    public static final int CBOR_TAG = 24;
}
