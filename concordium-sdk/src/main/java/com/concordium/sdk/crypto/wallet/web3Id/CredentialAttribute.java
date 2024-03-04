package com.concordium.sdk.crypto.wallet.web3Id;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
public final class CredentialAttribute {
    public enum CredentialAttributeType {
        INT,
        STRING,
        TIMESTAMP;
    }

    private String value;
    private CredentialAttributeType type;

    private int compareStringAttributes(byte[] aBytes, byte[] bBytes) {
        if (aBytes.length < bBytes.length) return -1;
        if (aBytes.length > bBytes.length) return 1;
    
        for (int i = 0; i < aBytes.length; i++) {
            byte aByte = aBytes[i];
            byte bByte = bBytes[i];
    
            if (aByte == bByte) continue;
            return aByte < bByte ? -1 : 1;
        }
    
        return 0;
    }

    public boolean isBetween(CredentialAttribute lower, CredentialAttribute upper) throws IllegalArgumentException {
        if (!this.getType().equals(lower.getType()) || !this.getType().equals(upper.getType())) {
            throw new IllegalArgumentException("Attribute types must match");
        }
        switch (this.type) {
            case INT: {
                long lowerVal = Long.parseUnsignedLong(lower.getValue());
                long upperVal =  Long.parseUnsignedLong(upper.getValue());
                long val = Long.parseUnsignedLong(this.getValue());
                return Long.compareUnsigned(lowerVal, val) <= 0 && Long.compareUnsigned(upperVal, val) > 0;
            }
            case TIMESTAMP: {
                LocalDateTime lowerVal = LocalDateTime.parse(lower.getValue());
                LocalDateTime upperVal = LocalDateTime.parse(upper.getValue());
                LocalDateTime val = LocalDateTime.parse(this.getValue());
                return !lowerVal.isAfter(val) && upperVal.isAfter(val);
            }
            case STRING: {
                byte[] lowerVal = lower.getValue().getBytes(StandardCharsets.UTF_8);
                byte[] upperVal = upper.getValue().getBytes(StandardCharsets.UTF_8);
                byte[] val = this.getValue().getBytes(StandardCharsets.UTF_8);
                return this.compareStringAttributes(val, lowerVal) >= 0 && this.compareStringAttributes(val, upperVal) < 0;
            }
            default:
                throw new IllegalArgumentException("Unknown attribute type");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CredentialAttribute)) {
            return false;
        }
        CredentialAttribute cred = (CredentialAttribute) obj;
        return this.type.equals(cred.type) && this.value.equals(cred.value);
    }

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
