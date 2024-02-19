package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonTypeName("web3Issuer")
public class Web3IssuerCommitmentInput extends CommitmentInput {
    private String signature;
    private String signer;
    private Map<String, CredentialAttribute> values;
    private Map<String, String> randomness;
}
