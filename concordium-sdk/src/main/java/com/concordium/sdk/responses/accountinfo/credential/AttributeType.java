package com.concordium.sdk.responses.accountinfo.credential;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Identity attributes of which a {@link Commitment} is generated for by the identity provider.
 * The {@link Commitment} for an attribute type is in turn part of the deployed {@link Credential} on chain.
 * Hence, the commitments that generated over these attributes are the public information sent to the chain as part of the {@link Credential},
 * which in turn is tied to an {@link com.concordium.sdk.responses.accountinfo.AccountInfo}.
 */
public enum AttributeType {
    @JsonProperty("firstName")
    FIRST_NAME,
    @JsonProperty("lastName")
    LAST_NAME,
    @JsonProperty("sex")
    SEX,
    @JsonProperty("dob")
    DOB,
    @JsonProperty("countryOfResidence")
    COUNTRY_OF_RESIDENCE,
    @JsonProperty("nationality")
    NATIONALITY,
    @JsonProperty("idDocType")
    ID_DOC_TYPE,
    @JsonProperty("idDocNo")
    ID_DOC_NO,
    @JsonProperty("idDocIssuer")
    ID_DOC_ISSUER,
    @JsonProperty("idDocIssuedAt")
    ID_DOC_ISSUED_AT,
    @JsonProperty("idDocExpiresAt")
    ID_DOC_EXPIRES_AT,
    @JsonProperty("nationalIdNo")
    NATIONAL_ID_NO,
    @JsonProperty("taxIdNo")
    TAX_ID_NO,
    @JsonProperty("lei")
    LEI,
    @JsonProperty("legalName")
    LEGAL_NAME,
    @JsonProperty("legalCountry")
    LEGAL_COUNTRY,
    @JsonProperty("businessNumber")
    BUSINESS_NUMBER,
    @JsonProperty("registrationAuth")
    REGISTRATION_AUTH,
    ;

    public static AttributeType fromJSON(String jsonStr) throws JsonMappingException, JsonProcessingException {
        return JsonMapper.INSTANCE.readValue(String.format("\"%s\"", jsonStr), AttributeType.class);
    }
}
