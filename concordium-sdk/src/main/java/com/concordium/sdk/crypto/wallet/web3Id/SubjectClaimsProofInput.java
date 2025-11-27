package com.concordium.sdk.crypto.wallet.web3Id;

import java.beans.Transient;

public interface SubjectClaimsProofInput {
    @Transient
    CommitmentInput getCommitmentInput();
}
