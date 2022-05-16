package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.transactions.Index;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

/**
 * Exists only for annotation driven deserializing.
 * See {@link Credential}
 */
@Getter
@ToString
final class Contents {
    /**
     * Identity of the identity provider who signed the identity object from
     * which this credential is derived.
     */
    private final int ipIdentity;
    /**
     * Revocation threshold. Any set of this many anonymity revokers can reveal IdCredPub.
     */
    private final int revocationThreshold;
    /**
     * The credential registration id
     */
    private final CredentialRegistrationId credId;
    /**
     * Policy.
     * Note. At the moment only opening of specific commitments.
     */
    private final Policy policy;
    /**
     * Commitments associated with this credential.
     */
    private final Commitments commitments;
    /**
     * The public keys for the credential including its `threshold`.
     */
    private final CredentialPublicKeys credentialPublicKeys;
    /**
     * Anonymity revocation data associated with this credential.
     */
    private final Map<Index, ArData> arData;

    @JsonCreator
    Contents(@JsonProperty("ipIdentity") int ipIdentity,
             @JsonProperty("revocationThreshold") int revocationThreshold,
             @JsonProperty("credId") String credId,
             @JsonProperty("policy") Policy policy,
             @JsonProperty("commitments") Commitments commitments,
             @JsonProperty("credentialPublicKeys") CredentialPublicKeys credentialPublicKeys,
             @JsonProperty("arData") Map<Index, ArData> arData) {
        this.ipIdentity = ipIdentity;
        this.revocationThreshold = revocationThreshold;
        this.credId = CredentialRegistrationId.from(credId);
        this.policy = policy;
        this.commitments = commitments;
        this.credentialPublicKeys = credentialPublicKeys;
        this.arData = arData;
    }
}
