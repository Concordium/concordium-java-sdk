package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;

import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 *  The data needed to create a web3Id Presentation.
 */
@Getter
@Builder
@Jacksonized
public class Web3IdProofInput {
    private final QualifiedRequest request;
    private final List<CommitmentInput> commitmentInputs;
    private final CryptographicParameters globalContext;
}
