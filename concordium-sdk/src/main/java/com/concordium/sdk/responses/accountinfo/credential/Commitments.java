package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class Commitments {
    private final String cmmPrf;
    private final String cmmCredCounter;
    private final List<String> cmmIdCredSecSharingCoeff;
    private final Attributes cmmAttributes;
    private final String cmmMaxAccounts;

    @JsonCreator
    Commitments(@JsonProperty("cmmPrf") String cmmPrf,
                @JsonProperty("cmmCredCounter") String cmmCredCounter,
                @JsonProperty("cmmIdCredSecSharingCoeff") List<String> cmmIdCredSecSharingCoeff,
                @JsonProperty("cmmAttributes") Attributes cmmAttributes,
                @JsonProperty("cmmMaxAccounts") String cmmMaxAccounts) {
        this.cmmPrf = cmmPrf;
        this.cmmCredCounter = cmmCredCounter;
        this.cmmIdCredSecSharingCoeff = cmmIdCredSecSharingCoeff;
        this.cmmAttributes = cmmAttributes;
        this.cmmMaxAccounts = cmmMaxAccounts;
    }
}
