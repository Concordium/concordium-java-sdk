package com.concordium.sdk.crypto.wallet.credential;

import com.concordium.sdk.transactions.TransactionExpiry;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialDeploymentDetails {

    private UnsignedCredentialDeploymentInfo unsignedCdi;
    
    @JsonUnwrapped
    private TransactionExpiry expiry;
    
}
