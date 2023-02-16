package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    LEI;
}
