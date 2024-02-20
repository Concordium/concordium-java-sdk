package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.QualifiedRequestStatement;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class QualifiedRequest {
    private final String challenge;
    private final List<QualifiedRequestStatement> credentialStatements;
}
