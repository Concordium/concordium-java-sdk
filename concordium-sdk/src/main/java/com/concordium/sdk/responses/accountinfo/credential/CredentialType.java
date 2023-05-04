package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CredentialType
 * A {@link Credential} can either be {@link CredentialType#INITIAL} if it is the first credential created
 * for the identity.
 * Subsequent credentials created for the identity has type {@link CredentialType#NORMAL}
 */
public enum CredentialType {
    @JsonProperty("initial")
    INITIAL,
    @JsonProperty("normal")
    NORMAL;

    public static CredentialType parse(com.concordium.grpc.v2.CredentialType credentialType) {
        switch (credentialType) {
            case CREDENTIAL_TYPE_INITIAL: return CredentialType.INITIAL;
            case CREDENTIAL_TYPE_NORMAL: return CredentialType.NORMAL;
            default: throw new IllegalArgumentException();
        }
    }
}
