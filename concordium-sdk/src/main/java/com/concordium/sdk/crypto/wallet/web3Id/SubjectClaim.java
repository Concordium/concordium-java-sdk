package com.concordium.sdk.crypto.wallet.web3Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A claim to prove.
 * <p>
 * Currently, the only claim type is {@link IdentityClaim}.
 */
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
