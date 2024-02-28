package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class RequestStatement {
    private String id;
    private List<String> type;
    private List<AtomicStatement> statement;
}
