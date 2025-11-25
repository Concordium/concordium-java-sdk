package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class IdentityQualifyingIdentityClaim implements QualifiedSubjectClaim {
    @JsonProperty("issuer")
    private final IdentityProviderRequestIdentifier id;

    private final IdentityCommitmentInput commitmentInput;

    private final List<AtomicStatement> statements;

    public String[] getType() {
        return TYPE;
    }

    public static final String[] TYPE = {"ConcordiumSubjectClaimsV1", "ConcordiumIdBasedSubjectClaims"};
}
