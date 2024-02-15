package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("AttributeNotInSet")
public class NonMembershipStatement extends AtomicStatement {
    private String attributeTag;
    private List<CredentialAttribute> set;
    
    @Override
    public boolean canBeProvedBy(IdentityObject identityObject) {
        try {
            CredentialAttribute value = this.getAttributeValue(identityObject);
            return !set.contains(value);
        } catch (Exception e) {
            return false;
        }
    }
}
