package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.primitives.Bytes;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L172">SetMetadataUrlParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class SetMetadataUrlParams extends SchemaParameter {

    private final String url;

    @JsonSerialize(using = SetMetadataUrlParamsHashSerializer.class)
    private final Optional<Hash> hash;
    public SetMetadataUrlParams(Schema schema, ReceiveName receiveName, String url, Optional<Hash> hash) {
        super(schema, receiveName);
        this.url = url;
        this.hash = hash;
    }

    public static class SetMetadataUrlParamsHashSerializer extends JsonSerializer<Optional<Hash>> {
        @Override
        public void serialize(Optional<Hash> hash, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            if (hash.isPresent()) {
                jsonGenerator.writeFieldName("Some");
                jsonGenerator.writeStartArray();

                jsonGenerator.writeStartArray();
                List<Byte> bytes = Bytes.asList(hash.get().getBytes());
                bytes.forEach(b -> {
                    try {
                        jsonGenerator.writeNumber(Byte.toUnsignedInt(b));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                jsonGenerator.writeEndArray();

                jsonGenerator.writeEndArray();
            } else {
                jsonGenerator.writeStringField("None", "");
            }
            jsonGenerator.writeEndObject();
        }
    }
}
