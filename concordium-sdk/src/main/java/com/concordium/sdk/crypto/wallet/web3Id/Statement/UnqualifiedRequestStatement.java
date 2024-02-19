package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.Network;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class UnqualifiedRequestStatement extends RequestStatement {
    private IdQualifier idQualifier;

    public QualifiedRequestStatement qualify(String credId, Network network) {
        String did = "did:ccd:" + network.getValue().toLowerCase() + ":cred:" + credId; 
        return QualifiedRequestStatement.builder().id(did).statement(getStatement()).build();        
    }
}
