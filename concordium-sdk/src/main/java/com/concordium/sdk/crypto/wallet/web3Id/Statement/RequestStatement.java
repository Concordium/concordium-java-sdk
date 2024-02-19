package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;
import java.util.stream.Collectors;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class RequestStatement {
    private List<String> type;
    private List<AtomicStatement> statement;

    public List<AtomicStatement> getUnsatisfiedStatements(IdentityObject identityObject) {
        return statement.stream().filter(s -> {
            try {
                return !s.canBeProvedBy(identityObject);
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    public boolean canBeProvedBy(IdentityObject identityObject) throws Exception {
        for (AtomicStatement s: this.statement) {
            if (s.canBeProvedBy(identityObject)) continue;
            return false;
        }
        return true;        
    }
}
