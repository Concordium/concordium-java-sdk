package com.example.android_sdk_example.identity_object;

import com.concordium.sdk.transactions.Signature;

public class IdentityObject {
    private AttributeList attributeList;
    private PreIdentityObject preIdentityObject;
    private String signature;

    public AttributeList getAttributeList() {
        return attributeList;
    }

    public PreIdentityObject getPreIdentityObject() {
        return preIdentityObject;
    }

    public String getSignature() {
        return signature;
    }
}

