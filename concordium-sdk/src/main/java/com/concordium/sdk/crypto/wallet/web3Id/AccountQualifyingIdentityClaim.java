package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.AccountRequestIdentifier;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AccountQualifyingIdentityClaim implements QualifiedSubjectClaim {
    private final String[] type = {"ConcordiumSubjectClaimsV1", "ConcordiumAccountBasedSubjectClaims"};

    private final AccountRequestIdentifier id;
    private final IdentityProviderRequestIdentifier issuer;
    private final AccountCommitmentInput commitmentInput;
    private final List<AtomicStatement> statements;
}
