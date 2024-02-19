package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class QualifiedRequestStatement extends RequestStatement {
    private String id;
}
