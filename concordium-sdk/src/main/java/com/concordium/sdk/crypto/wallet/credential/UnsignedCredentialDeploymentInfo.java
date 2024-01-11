package com.concordium.sdk.crypto.wallet.credential;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.Policy;
import com.concordium.sdk.transactions.CredentialPublicKeys;

import lombok.Getter;

@Getter
public class UnsignedCredentialDeploymentInfo {

    private int ipIdentity;
    private CredentialPublicKeys credentialPublicKeys;
    private Policy policy;
    private String credId;
    private int threshold;
    private Map<String, ChainArData> arData;
    private IdOwnershipProofs proofs;
}
