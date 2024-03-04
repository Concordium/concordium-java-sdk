package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.RequestIdentifier;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class QualifiedRequestStatement extends RequestStatement {
    private RequestIdentifier id;
    
    public StatementType getStatementType() {
        return this.id.getType();
    }
}
