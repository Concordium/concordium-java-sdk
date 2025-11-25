package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.util.Map;

@Getter
@JsonTypeName("identity")
public class IdentityCommitmentInput extends CommitmentInput {
    private final IdentityProviderInfo ipInfo;
    private final Map<String, AnonymityRevokerInfo> arsInfos;
    private final IdentityObject idObject;
    private final JsonNode idObjectUseData;

    /**
     * @param ipInfo             identity provider that issued the identity, stored in the wallet or fetched from a node
     * @param arsInfos           anonymity revokers of the identity provider, stored in the wallet or fetched from a node
     * @param idObject           identity itself, stored in the wallet after creation or recovery
     * @param idCredSec          cred sec for the identity, derived with the HD wallet
     * @param prfKey             PRF key for the identity, derived with the HD wallet
     * @param blindingRandomness signature blinding randomness for the identity, derived with the HD wallet
     * @see com.concordium.sdk.ClientV2#getIdentityProviders(BlockQuery)
     * @see com.concordium.sdk.ClientV2#getAnonymityRevokers(BlockQuery)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getIdCredSec(int, int)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getPrfKey(int, int)
     * @see com.concordium.sdk.crypto.wallet.ConcordiumHdWallet#getSignatureBlindingRandomness(int, int)
     */
    public IdentityCommitmentInput(@NonNull
                                   IdentityProviderInfo ipInfo,
                                   @NonNull
                                   Map<String, AnonymityRevokerInfo> arsInfos,
                                   @NonNull
                                   IdentityObject idObject,
                                   @NonNull
                                   BLSSecretKey idCredSec,
                                   @NonNull
                                   BLSSecretKey prfKey,
                                   @NonNull
                                   String blindingRandomness) {
        this.ipInfo = ipInfo;
        this.arsInfos = arsInfos;
        this.idObject = idObject;

        val credentialHolderInformation = JsonMapper.INSTANCE.createObjectNode();
        credentialHolderInformation.putPOJO("idCredSecret", idCredSec);

        val aci = JsonMapper.INSTANCE.createObjectNode();
        aci.putPOJO("prfKey", prfKey);
        aci.set("credentialHolderInformation", credentialHolderInformation);

        val idObjectUseData = JsonMapper.INSTANCE.createObjectNode();
        idObjectUseData.put("randomness", blindingRandomness);
        idObjectUseData.set("aci", aci);

        this.idObjectUseData = idObjectUseData;
    }
}
