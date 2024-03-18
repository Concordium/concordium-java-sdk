package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Getter;

@Getter
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({ @Type(value = IdentityQualifier.class), @Type(value = VerifiableCredentialQualifier.class) })
public class IdQualifier {
    @JsonUnwrapped
    StatementType type;
}
