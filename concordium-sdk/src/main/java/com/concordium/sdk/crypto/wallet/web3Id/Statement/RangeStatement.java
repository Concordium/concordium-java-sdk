package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("AttributeInRange")
public class RangeStatement extends AtomicStatement {
    private final CredentialAttribute lower;
    private final CredentialAttribute upper;
    private final String attributeTag;

    @Override
    @SneakyThrows
    public boolean canBeProvedBy(IdentityObject identityObject) {
        try {
            CredentialAttribute value = getAttributeValue(identityObject);
            return value.isBetween(lower, upper);
        } catch (MissingAttributeException e) {
            // If the identityObject does not have the relevant attribute, it does not satisfy the statement 
            return false;
        }

    }
}
