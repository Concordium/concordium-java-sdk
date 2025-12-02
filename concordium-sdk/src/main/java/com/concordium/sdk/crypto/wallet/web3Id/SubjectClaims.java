package com.concordium.sdk.crypto.wallet.web3Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Some claims to prove.
 * <p>
 * Currently, the only known claims are {@link IdentityClaims}.
 */
@JsonTypeInfo(
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes(
        @JsonSubTypes.Type(
                value = IdentityClaims.class,
                name = IdentityClaims.TYPE
        )
)
public interface SubjectClaims {
}
