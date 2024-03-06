package com.concordium.sdk.crypto.wallet.identityobject;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

import lombok.Getter;

@Getter
public class MissingAttributeException extends Exception {
    private AttributeType attribute;

    public MissingAttributeException(AttributeType attribute) {
        this.attribute = attribute;
    }
}
