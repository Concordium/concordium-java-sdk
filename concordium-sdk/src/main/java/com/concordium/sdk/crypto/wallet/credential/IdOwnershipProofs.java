package com.concordium.sdk.crypto.wallet.credential;

import java.util.Map;

import lombok.Getter;

@Getter
public class IdOwnershipProofs {
    
    private String challenge;
    private String commitments;
    private String credCounterLessThanMaxAccounts;
    private Map<String, String> proofIdCredPub;
    private String proofIpSig;
    private String proofRegId;
    private String sig;

}
