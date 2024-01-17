package com.concordium.sdk.crypto.wallet;

import java.util.List;
import java.util.Map;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.transactions.CredentialPublicKeys;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class UnsignedCredentialInput {
    
    @NonNull
    private IdentityProviderInfo ipInfo;

    @NonNull
    private CryptographicParameters globalContext;

    @NonNull
    private Map<String, AnonymityRevokerInfo> arsInfos;

    @NonNull
    private IdentityObject idObject;

    @NonNull
    private List<AttributeType> revealedAttributes;

    private int credNumber;

    @NonNull
    private BLSSecretKey idCredSec;

    @NonNull
    private BLSSecretKey prfKey;

    @NonNull
    private String blindingRandomness;

    @NonNull
    private Map<AttributeType, String> attributeRandomness;

    @NonNull
    private CredentialPublicKeys credentialPublicKeys;
    
}
