package com.concordium.sdk.crypto.wallet.credential;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

public class CredentialDeploymentCommitments {

    private String cmmPrf;
    private String cmmCredCounter;
    private String[] cmmIdCredSecSharingCoeff;
    private Map<AttributeType, String> cmmAttributes;
    private String cmmMaxAccounts;

}
