package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class Request {
    private final String challenge;
    private final List<RequestStatement> credentialStatements;
}
