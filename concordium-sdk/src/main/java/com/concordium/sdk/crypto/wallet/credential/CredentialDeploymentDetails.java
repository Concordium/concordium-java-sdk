package com.concordium.sdk.crypto.wallet.credential;

import com.concordium.sdk.transactions.TransactionExpiry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialDeploymentDetails {

    private UnsignedCredentialDeploymentInfo cdi;
    private TransactionExpiry expiry;
    
}
