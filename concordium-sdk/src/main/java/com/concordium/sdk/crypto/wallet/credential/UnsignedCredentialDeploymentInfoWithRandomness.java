package com.concordium.sdk.crypto.wallet.credential;

import lombok.Getter;

@Getter
public class UnsignedCredentialDeploymentInfoWithRandomness {

    private UnsignedCredentialDeploymentInfo unsignedCdi;
    private CommitmentRandomness randomness;

}
