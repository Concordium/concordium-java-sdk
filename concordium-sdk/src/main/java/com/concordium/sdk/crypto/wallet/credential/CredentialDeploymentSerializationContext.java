package com.concordium.sdk.crypto.wallet.credential;

import java.util.Map;

import com.concordium.sdk.transactions.Index;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class CredentialDeploymentSerializationContext {
    @NonNull
    private final UnsignedCredentialDeploymentInfo unsignedCdi;

    @NonNull
    private final Map<Index, String> signatures;
}
