package com.concordium.sdk.crypto.wallet;

import java.util.Map;

import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IdentityRequestInput {

    private CryptographicParameters globalContext;
    private Map<String, AnonymityRevokerInfo> arsInfos;
    private IdentityProviderInfo ipInfo;
    private long arThreshold;
    private String idCredSec;
    private String prfKey;
    private String blindingRandomness;

}
