package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Policy {
    private final String createdAt;
    private final String validTo;
    private final Attributes revealedAttributes;

    @JsonCreator
    Policy(@JsonProperty("createdAt") String createdAt,
           @JsonProperty("validTo") String validTo,
           @JsonProperty("revealedAttributes") Attributes revealedAttributes) {
        this.createdAt = createdAt;
        this.validTo = validTo;
        this.revealedAttributes = revealedAttributes;
    }
}
