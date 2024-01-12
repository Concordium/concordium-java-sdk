package com.concordium.sdk.crypto.wallet.credential;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CredentialDeploymentSerializationContext {
    private UnsignedCredentialDeploymentInfo unsignedCdi;
    private List<String> signatures;
}
