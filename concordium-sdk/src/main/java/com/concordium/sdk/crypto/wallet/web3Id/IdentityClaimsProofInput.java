package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.RequestIdentifier;

import java.util.List;

public interface IdentityClaimsProofInput extends SubjectClaimsProofInput {
    String[] getType();
    RequestIdentifier getId();
    List<AtomicStatement> getStatement();
}
