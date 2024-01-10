package com.concordium.sdk.crypto.wallet.identityobject;

import lombok.Getter;

@Getter
public class IdentityObject {

    private AttributeList attributeList;
    private PreIdentityObject preIdentityObject;
    private String signature;

}
