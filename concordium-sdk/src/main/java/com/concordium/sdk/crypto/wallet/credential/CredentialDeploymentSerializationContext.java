package com.concordium.sdk.crypto.wallet.credential;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CredentialDeploymentSerializationContext {
    @NonNull
    private final UnsignedCredentialDeploymentInfo unsignedCdi;

    @NonNull
    private final List<String> signatures;
}
