package com.concordium.sdk.crypto.wallet.credential;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

import lombok.Getter;

@Getter
public class CommitmentRandomness {
    
    private String idCredSecRand;
    private String prfRand;
    private String credCounterRand;
    private String maxAccountsRand;
    private Map<AttributeType, String> attributesRand;

}
