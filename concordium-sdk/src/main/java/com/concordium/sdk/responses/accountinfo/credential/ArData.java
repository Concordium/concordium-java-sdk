package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Anonymity revoker data
 */
@Getter
@ToString
public final class ArData {
    private final String encIdCredPubShare;

    @JsonCreator
    ArData(@JsonProperty("encIdCredPubShare") String encIdCredPubShare) {
        this.encIdCredPubShare = encIdCredPubShare;
    }
}
