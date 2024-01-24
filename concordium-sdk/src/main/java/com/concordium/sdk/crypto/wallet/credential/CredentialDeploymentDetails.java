package com.concordium.sdk.crypto.wallet.credential;

import com.concordium.sdk.transactions.Expiry;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CredentialDeploymentDetails {

    @NonNull
    private final UnsignedCredentialDeploymentInfo unsignedCdi;
    
    @NonNull
    @JsonUnwrapped
    private final Expiry expiry;
    
}
