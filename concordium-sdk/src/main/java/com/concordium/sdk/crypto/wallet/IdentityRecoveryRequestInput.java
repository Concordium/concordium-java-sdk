package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IdentityRecoveryRequestInput {

    private IdentityProviderInfo ipInfo;
    private CryptographicParameters globalContext;
    private long timestamp;
    BLSSecretKey idCredSec;

}
