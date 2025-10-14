package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.fasterxml.jackson.dataformat.cbor.CBORParser;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Getter
@JsonSerialize(using = TaggedTokenHolderAccount.CborSerializer.class)
@JsonDeserialize(using = TaggedTokenHolderAccount.CborDeserializer.class)
public class TaggedTokenHolderAccount {

    private final byte[] data;

    private TaggedTokenHolderAccount(byte[] data) {
        if (data.length != AccountAddress.BYTES) {
            throw new IllegalArgumentException("The address data has invalid size " + data.length);
        }
        this.data = data;
    }

    public TaggedTokenHolderAccount(AccountAddress accountAddress) {
        this(accountAddress.getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaggedTokenHolderAccount)) return false;
        TaggedTokenHolderAccount that = (TaggedTokenHolderAccount) o;
        return Objects.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    static class CborSerializer extends StdSerializer<TaggedTokenHolderAccount> {

        protected CborSerializer() {
            super(TaggedTokenHolderAccount.class);
        }

        @Override
        public void serialize(TaggedTokenHolderAccount taggedTokenHolderAccount,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;
            cborGenerator.writeTag(CBOR_TAG);
            cborGenerator.writeStartObject(taggedTokenHolderAccount, 1);
            cborGenerator.writeFieldId(BYTES_FIELD_ID);
            cborGenerator.writeObject(taggedTokenHolderAccount.data);
            cborGenerator.writeEndObject();
        }
    }

    static class CborDeserializer extends StdDeserializer<TaggedTokenHolderAccount> {

        protected CborDeserializer() {
            super(TaggedTokenHolderAccount.class);
        }

        @Override
        public TaggedTokenHolderAccount deserialize(JsonParser jsonParser,
                                                    DeserializationContext ctxt) throws IOException {
            val cborParser = (CBORParser) jsonParser;
            val currentTag = cborParser.getCurrentTag();
            if (currentTag != CBOR_TAG) {
                throw new JsonParseException(
                        jsonParser,
                        "Expected token holder account tag " + CBOR_TAG + ", but read " + currentTag
                );
            }
            val objectNode = cborParser.readValueAs(ObjectNode.class);
            val dataNode = objectNode.get(String.valueOf(BYTES_FIELD_ID));
            if (dataNode == null || !dataNode.isBinary()) {
                throw new JsonParseException(
                        jsonParser,
                        "Missing or invalid account address bytes"
                );
            }
            return new TaggedTokenHolderAccount(dataNode.binaryValue());
        }
    }

    private static final int CBOR_TAG = 40307;
    private static final int BYTES_FIELD_ID = 3;
}
