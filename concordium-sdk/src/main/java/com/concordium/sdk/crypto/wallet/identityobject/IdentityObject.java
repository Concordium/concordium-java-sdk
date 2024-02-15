package com.concordium.sdk.crypto.wallet.identityobject;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

import lombok.Getter;
import lombok.val;

@Getter
public class IdentityObject {

    private AttributeList attributeList;
    private PreIdentityObject preIdentityObject;
    private String signature;

    public String getChosenAttribute(AttributeType attribute) throws MissingAttributeException {
        val chosenAttributes = this.getAttributeList().getChosenAttributes();
        if (!chosenAttributes.containsKey(attribute)) {
            throw new MissingAttributeException(attribute);
        }
        return chosenAttributes.get(attribute);        
    } 
}
