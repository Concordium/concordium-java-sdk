package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.collect.ImmutableMap;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

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
    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT)
    private YearMonth createdAt;

    /**
     * The last year and month when the credential is still valid. After this
     * expires an account can no longer be created from the credential.
     */
    @JsonProperty("validTo")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FORMAT)
    private YearMonth validTo;

    @JsonProperty("revealedAttributes")
    @Singular
    private final Map<AttributeType, String> revealedAttributes;

    /**
     * Mapping from attribute tags to attribute values. Attribute tags are always
     * representable in a single `u8`, attribute values are never more than 31 bytes in length.
     */
    public ImmutableMap<AttributeType, String> getRevealedAttributes() {
        return copyOf(revealedAttributes);
    }

    @JsonSetter
    void setCreatedAt(String str) {
        this.createdAt = YearMonth.parse(str, DateTimeFormatter.ofPattern(FORMAT));
    }

    @JsonSetter
    void setValidTo(String str) {
        this.validTo = YearMonth.parse(str, DateTimeFormatter.ofPattern(FORMAT));
    }
}
