package com.concordium.sdk.crypto.wallet.web3Id;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;
import lombok.Getter;

@JsonSerialize(using = CredentialAttribute.CredentialAttributeTypeSerializer.class)
@JsonDeserialize(using = CredentialAttribute.CredentialAttributieTypeDeserializer.class)
@Builder
@Getter
public class CredentialAttribute {
    enum CredentialAttributeType {
        INT,
        STRING,
        TIMESTAMP;
    }

    private String value;
    private CredentialAttributeType type;

    static class CredentialAttributieTypeDeserializer extends JsonDeserializer<CredentialAttribute> {

        @Override
        public CredentialAttribute deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            JsonNode reference = p.getCodec().readTree(p);
            if (reference.isNumber()) {
                return new CredentialAttribute(reference.asText(), CredentialAttributeType.INT);
            } else if (reference.isTextual()) {
                return new CredentialAttribute(reference.asText(), CredentialAttributeType.STRING);
            } else if (reference.has("type") && reference.get("type").asText().equals("date-time")
                    && reference.has("timestamp")) {
                return new CredentialAttribute(reference.get("timestamp").asText(), CredentialAttributeType.TIMESTAMP);
            }
            throw new JsonParseException(p, "Instantiation of Web3IdAtttribute failed due to invalid structure");
        }
    }

    static class CredentialAttributeTypeSerializer extends JsonSerializer<CredentialAttribute> {
        @Override
        public void serialize(CredentialAttribute value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            switch (value.getType()) {
                case INT:
                    gen.writeNumber(value.getValue());
                    break;
                case STRING:
                    gen.writeString(value.getValue());
                    break;
                case TIMESTAMP:
                    gen.writeStartObject();
                    gen.writeStringField("type", "date-time");
                    gen.writeStringField("timestamp", value.getValue());
                    gen.writeEndObject();
                    break;
                default:
                    // The switch already covers every case of the given type, so this should never
                    // happen
                    throw new RuntimeException("Unreachable, invalid CredentialAttributeType: " + value.getType());
            }
        }

    }
}
