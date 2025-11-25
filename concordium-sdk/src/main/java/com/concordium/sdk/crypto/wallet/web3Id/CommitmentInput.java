package com.concordium.sdk.crypto.wallet.web3Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = AccountCommitmentInput.class),
        @Type(value = Web3IssuerCommitmentInput.class),
        @Type(value = IdentityCommitmentInput.class)
})
public abstract class CommitmentInput {}
