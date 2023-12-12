package com.concordium.sdk.responses.accountinfo.credential;


/**
 * CredentialType
 * A {@link Credential} can either be {@link CredentialType#INITIAL} if it is the first credential created
 * for the identity.
 * Subsequent credentials created for the identity has type {@link CredentialType#NORMAL}
 */
public enum CredentialType {
    INITIAL,
    NORMAL
}
