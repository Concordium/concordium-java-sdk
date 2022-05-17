package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.transactions.Index;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * A credential on the chain.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class Credential {
    /**
     * The version.
     */
    private final int version;

    /**
     * The credential type.
     */
    private final CredentialType type;

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
    Credential(@JsonProperty("v") int version,
               @JsonProperty("value") Value value) {
        this.version = version;
        this.type = value.getType();
        this.ipIdentity = value.getContents().getIpIdentity();
        this.revocationThreshold = value.getContents().getRevocationThreshold();
        this.credId = value.getContents().getRegistrationId();
        this.policy = value.getContents().getPolicy();
        this.commitments = value.getContents().getCommitments();
        this.credentialPublicKeys = value.getContents().getCredentialPublicKeys();
        this.arData = value.getContents().getArData();
    }
}
