package com.concordium.sdk.responsetypes.accountinfo.credential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
class Contents {
    private int ipIdentity;
    private int revocationThreshold;
    private String credId;
    private Policy policy;
    private Commitments commitments;
    private CredentialPublicKeys credentialPublicKeys;
    private Map<String, ArData> arData;
}
