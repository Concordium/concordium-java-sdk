package com.concordium.sdk.crypto.wallet;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class IdentityRecoveryRequestInput {

    @NonNull
    private IdentityProviderInfo ipInfo;

    @NonNull
    private CryptographicParameters globalContext;

    @NonNull
    BLSSecretKey idCredSec;

    private long timestamp;

}
