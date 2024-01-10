package com.concordium.sdk.crypto.wallet.identityobject;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

import lombok.Getter;

@Getter
public class AttributeList {
    
    private Map<AttributeType, String> chosenAttributes;
    String validTo;
    String createdAt;
    int maxAccounts;

}
