package com.concordium.sdk.crypto.wallet.web3Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AccountCommitmentInput.class),
        @JsonSubTypes.Type(value = Web3IssuerCommitmentInput.class),
        @JsonSubTypes.Type(value = IdentityCommitmentInput.class)
})
public abstract class CommitmentInput {
}
