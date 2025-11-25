package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes(
        @JsonSubTypes.Type(
                value = IdentityClaim.class,
                name = IdentityClaim.TYPE
        )
)
public interface SubjectClaim {
    List<AtomicStatement> getStatements();
}
