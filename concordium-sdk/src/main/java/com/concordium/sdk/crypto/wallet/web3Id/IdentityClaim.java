package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Statement requesting proofs from identity credentials issued by identity providers.
 * Can specify whether to accept proofs from identity credentials, account credentials, or both.
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class IdentityClaim implements SubjectClaim {

    /**
     * Credential types accepted for this statement (identity credential, account credential, or both).
     *
     * @see IdentityClaim#IDENTITY_CREDENTIAL_SOURCE
     * @see IdentityClaim#ACCOUNT_CREDENTIAL_SOURCE
     */
    private final List<String> source;

    /**
     * Atomic statements about identity attributes to prove.
     */
    @Singular
    private final List<AtomicStatement> statements;

    /**
     * Valid identity issuers for this statement, in form of DIDs.
     * For example, <code>did:ccd:testnet:idp:0</code>
     */
    @Singular
    private final List<String> issuers;

    public static final String TYPE = "identity";
    public static final String IDENTITY_CREDENTIAL_SOURCE = "identityCredential";
    public static final String ACCOUNT_CREDENTIAL_SOURCE = "accountCredential";
}
