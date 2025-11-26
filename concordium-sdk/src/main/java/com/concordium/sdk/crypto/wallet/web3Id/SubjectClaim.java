package com.concordium.sdk.crypto.wallet.web3Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
}
