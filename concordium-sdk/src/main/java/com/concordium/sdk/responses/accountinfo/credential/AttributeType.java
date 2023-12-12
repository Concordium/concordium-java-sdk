package com.concordium.sdk.responses.accountinfo.credential;

/**
 * Identity attributes of which a {@link Commitment} is generated for by the identity provider.
 * The {@link Commitment} for an attribute type is in turn part of the deployed {@link Credential} on chain.
 * Hence, the commitments that generated over these attributes are the public information sent to the chain as part of the {@link Credential},
 * which in turn is tied to an {@link com.concordium.sdk.responses.accountinfo.AccountInfo}.
 */
public enum AttributeType {
    FIRST_NAME,
    LAST_NAME,
    SEX,
    DOB,
    COUNTRY_OF_RESIDENCE,
    NATIONALITY,
    ID_DOC_TYPE,
    ID_DOC_NO,
    ID_DOC_ISSUER,
    ID_DOC_ISSUED_AT,
    ID_DOC_EXPIRES_AT,
    NATIONAL_ID_NO,
    TAX_ID_NO,
    LEI
}
