package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.copyOf;

/**
 * Policy on a {@link Credential}.
 */
@Getter
@ToString
@Builder
@Jacksonized
@EqualsAndHashCode
public final class Policy {
    private static final String FORMAT = "yyyyMM";

    /**
     * The year and month when the identity object from which the credential is derived was created.
     */
    @JsonDeserialize(using = Policy.YearMonthDeserializer.class)
    @JsonSerialize(using = Policy.YearMonthSerializer.class)
    private YearMonth createdAt;

    /**
     * The last year and month when the credential is still valid. After this
     * expires an account can no longer be created from the credential.
     */
    @JsonDeserialize(using = Policy.YearMonthDeserializer.class)
    @JsonSerialize(using = Policy.YearMonthSerializer.class)
    private YearMonth validTo;

    @Singular
    private final Map<AttributeType, String> revealedAttributes;

    /**
     * Mapping from attribute tags to attribute values. Attribute tags are always
     * representable in a single `u8`, attribute values are never more than 31 bytes in length.
     */
    public ImmutableMap<AttributeType, String> getRevealedAttributes() {
        return copyOf(revealedAttributes);
    }

    static class YearMonthDeserializer extends JsonDeserializer<YearMonth> {

        @Override
        public YearMonth deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String yearMonth = p.readValueAs(String.class);
            String year = yearMonth.subSequence(0, 4).toString();
            String month = yearMonth.substring(4, 6).toString();
            return YearMonth.parse(year + "-" + month);
        }
    }

    /**
     * A custom Jackson serializer is provided that makes the UInt32 JSON serialization
     * compatible with the JSON format expected by the Rust libraries.
     */
    static class YearMonthSerializer extends StdSerializer<YearMonth> {

        public YearMonthSerializer() {
            this(null);
        }

        public YearMonthSerializer(Class<YearMonth> t) {
            super(t);
        }

        @Override
        public void serialize(
                YearMonth yearMonth, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString(yearMonth.format(DateTimeFormatter.ofPattern(FORMAT)));
        }
    }
}
