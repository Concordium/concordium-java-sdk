package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("AttributeInSet")
public class MembershipStatement extends AtomicStatement {
    private String attributeTag;
    private List<CredentialAttribute> set;
    
    @Override
    public boolean canBeProvedBy(IdentityObject identityObject) throws Exception {
        try {
            CredentialAttribute value = this.getAttributeValue(identityObject);
            return set.contains(value);
        } catch (MissingAttributeException e) {
            // If the identityObject does not have the relevant attribute, it does not satisfy the statement 
            return false;
        }
    }
}
