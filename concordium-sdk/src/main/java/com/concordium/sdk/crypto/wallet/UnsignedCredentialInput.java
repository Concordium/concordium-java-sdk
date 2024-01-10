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

@Builder
@Getter
public class UnsignedCredentialInput {
    
    private IdentityProviderInfo ipInfo;
    private CryptographicParameters globalContext;
    private Map<String, AnonymityRevokerInfo> arsInfos;
    private IdentityObject idObject;
    private List<AttributeType> revealedAttributes;
    private int credNumber;
    private BLSSecretKey idCredSec;
    private BLSSecretKey prfKey;
    private String blindingRandomness;
    private Map<AttributeType, String> attributeRandomness;
    private CredentialPublicKeys credentialPublicKeys;
    
}
