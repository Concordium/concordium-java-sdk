package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public final class Contents {
    private final int ipIdentity;
    private final int revocationThreshold;
    private final String credId;
    private final Policy policy;
    private final Commitments commitments;
    private final CredentialPublicKeys credentialPublicKeys;
    private final Map<String, ArData> arData;

    @JsonCreator
    Contents(@JsonProperty("ipIdentity") int ipIdentity,
             @JsonProperty("revocationThreshold") int revocationThreshold,
             @JsonProperty("credId") String credId,
             @JsonProperty("policy") Policy policy,
             @JsonProperty("commitments") Commitments commitments,
             @JsonProperty("credentialPublicKeys") CredentialPublicKeys credentialPublicKeys,
             @JsonProperty("arData") Map<String, ArData> arData) {
        this.ipIdentity = ipIdentity;
        this.revocationThreshold = revocationThreshold;
        this.credId = credId;
        this.policy = policy;
        this.commitments = commitments;
        this.credentialPublicKeys = credentialPublicKeys;
        this.arData = arData;
    }
}
