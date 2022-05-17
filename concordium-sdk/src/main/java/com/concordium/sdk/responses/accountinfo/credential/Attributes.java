package com.concordium.sdk.responses.accountinfo.credential;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The attributes associated with an `Identity`
 * The attributes are commitments if they are present for the `Credential`.
 */
@Getter
@ToString
public final class Attributes {
    private final String firstName;
    private final String lastName;
    private final String sex;
    private final String dob;
    private final String countryOfResidence;
    private final String nationality;
    private final String idDocType;
    private final String idDocNo;
    private final String idDocIssuer;
    private final String idDocIssuedAt;
    private final String idDocExpiresAt;
    private final String nationalIdNo;
    private final String taxIdNo;
    private final String lei;

    @JsonCreator
    Attributes(@JsonProperty("firstName") String firstName,
               @JsonProperty("lastName") String lastName,
               @JsonProperty("sex") String sex,
               @JsonProperty("dob") String dob,
               @JsonProperty("countryOfResidence") String countryOfResidence,
               @JsonProperty("nationality") String nationality,
               @JsonProperty("idDocType") String idDocType,
               @JsonProperty("idDocNo") String idDocNo,
               @JsonProperty("idDocIssuer") String idDocIssuer,
               @JsonProperty("idDocIssuedAt") String idDocIssuedAt,
               @JsonProperty("idDocExpiresAt") String idDocExpiresAt,
               @JsonProperty("nationalIdNo") String nationalIdNo,
               @JsonProperty("taxIdNo") String taxIdNo,
               @JsonProperty("lei") String lei) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.dob = dob;
        this.countryOfResidence = countryOfResidence;
        this.nationality = nationality;
        this.idDocType = idDocType;
        this.idDocNo = idDocNo;
        this.idDocIssuer = idDocIssuer;
        this.idDocIssuedAt = idDocIssuedAt;
        this.idDocExpiresAt = idDocExpiresAt;
        this.nationalIdNo = nationalIdNo;
        this.taxIdNo = taxIdNo;
        this.lei = lei;
    }
}
