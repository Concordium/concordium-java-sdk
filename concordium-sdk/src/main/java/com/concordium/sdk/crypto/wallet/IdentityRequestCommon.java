package com.concordium.sdk.crypto.wallet;

import java.util.Map;

import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentityRequestCommon {

    private CryptographicParameters globalContext;
    private Map<String, AnonymityRevokerInfo> arsInfos;
    private IdentityProviderInfo ipInfo;
    private long arThreshold;
    
}
