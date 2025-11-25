package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.AccountRequestIdentifier;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AccountQualifyingIdentityClaim implements QualifiedSubjectClaim {
    private final AccountRequestIdentifier id;
    private final AccountCommitmentInput commitmentInput;
    private final List<AtomicStatement> statements;

    public String[] getType() {
        return TYPE;
    }

    public static final String[] TYPE = {"ConcordiumSubjectClaimsV1", "ConcordiumAccountBasedSubjectClaims"};
}
