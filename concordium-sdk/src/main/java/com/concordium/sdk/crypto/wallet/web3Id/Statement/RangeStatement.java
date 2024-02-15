package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@JsonTypeName("AttributeInRange")
@Getter
@Builder
@Jacksonized
public class RangeStatement extends AtomicStatement {
    private CredentialAttribute lower;
    private CredentialAttribute upper;
    private String attributeTag;
    @Override
    public boolean canBeProvedBy(IdentityObject identityObject) throws Exception {
        try {
            CredentialAttribute value = getAttributeValue(identityObject);
            return value.isBetween(lower, upper);
        } catch (MissingAttributeException e) {
            // If the identityObject does not have the relevant attribute, it does not satisfy the statement 
            return false;
        }

    }
}
