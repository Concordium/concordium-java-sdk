package com.example.android_sdk_example.identity_object;

import java.util.List;
import java.util.Map;

public class PreIdentityObject {

    ChoiceArData choiceArData;
    String idCredSecCommitment;
    Map<String, IpArData> ipArData;
    String prfKeyCommitmentWithIP;
    List<String> prfKeySharingCoeffCommitments;
    String proofsOfKnowledge;
    String idCredPub;

    public ChoiceArData getChoiceArData() {
        return choiceArData;
    }

    public String getIdCredSecCommitment() {
        return idCredSecCommitment;
    }

    public Map<String, IpArData> getIpArData() {
        return ipArData;
    }

    public String getPrfKeyCommitmentWithIP() {
        return prfKeyCommitmentWithIP;
    }

    public List<String> getPrfKeySharingCoeffCommitments() {
        return prfKeySharingCoeffCommitments;
    }

    public String getProofsOfKnowledge() {
        return proofsOfKnowledge;
    }

    public String getIdCredPub() {
        return idCredPub;
    }
}
