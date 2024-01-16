package com.concordium.sdk.crypto.wallet;

import java.util.Map;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class IdentityRequestInput {

    @NonNull
    private final CryptographicParameters globalContext;

    @NonNull
    private final Map<String, AnonymityRevokerInfo> arsInfos;
    
    @NonNull
    private final IdentityProviderInfo ipInfo;
    
    private final long arThreshold;
    
    @NonNull
    private final BLSSecretKey idCredSec;
    
    @NonNull
    private final BLSSecretKey prfKey;
    
    @NonNull
    private final String blindingRandomness;
}
