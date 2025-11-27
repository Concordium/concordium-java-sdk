package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.RequestIdentifier;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;
import java.util.List;

public interface QualifiedSubjectClaim {
    String[] getType();
    RequestIdentifier getId();
    @JsonProperty("statement")
    List<AtomicStatement> getStatementInput();
    @Transient
    CommitmentInput getCommitmentInput();
}
