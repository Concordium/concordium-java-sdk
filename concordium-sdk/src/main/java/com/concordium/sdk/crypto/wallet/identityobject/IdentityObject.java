package com.concordium.sdk.crypto.wallet.identityobject;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

import lombok.Getter;

@Getter
public class IdentityObject {

    private AttributeList attributeList;
    private PreIdentityObject preIdentityObject;
    private String signature;

    public String getChosenAttribute(AttributeType attribute) throws MissingAttributeException {
        Map<AttributeType, String> chosenAttributes = this.getAttributeList().getChosenAttributes();
        if (!chosenAttributes.containsKey(attribute)) {
            throw new MissingAttributeException(attribute);
        }
        return chosenAttributes.get(attribute);        
    } 
}
