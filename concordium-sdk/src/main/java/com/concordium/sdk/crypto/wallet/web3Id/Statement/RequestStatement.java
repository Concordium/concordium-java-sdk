package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;
import java.util.stream.Collectors;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class RequestStatement {
    @JsonProperty("type")
    private List<String> verifiableCredentialTypes;
    private List<AtomicStatement> statement;

    public abstract StatementType getStatementType();

    public List<AtomicStatement> getUnsatisfiedStatements(IdentityObject identityObject) {
        if (!this.getStatementType().equals(StatementType.Credential)) {
            throw new IllegalArgumentException("Only an account statement can be satisfied by an identity");
        }

        return statement.stream().filter(s -> {
            try {
                return !s.canBeProvedBy(identityObject);
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    public boolean canBeProvedBy(IdentityObject identityObject) throws Exception {
        if (!this.getStatementType().equals(StatementType.Credential)) {
            throw new IllegalArgumentException("Only an account statement can by proven using a identity");
        }

        for (AtomicStatement s : this.statement) {
            if (s.canBeProvedBy(identityObject))
                continue;
            return false;
        }
        return true;
    }
}
