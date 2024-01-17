package com.concordium.sdk.crypto.wallet.identityobject;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class PreIdentityObject {
    
    private ChoiceArData choiceArData;
    private String idCredSecCommitment;
    private Map<String, IpArData> ipArData;
    private String prfKeyCommitmentWithIP;
    private List<String> prfKeySharingCoeffCommitments;
    private String proofsOfKnowledge;
    private String idCredPub;
    
}
