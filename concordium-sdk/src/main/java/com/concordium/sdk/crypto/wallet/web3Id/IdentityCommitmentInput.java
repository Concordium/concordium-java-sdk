package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

import java.util.Map;

@Getter
@JsonTypeName("identity")
public class IdentityCommitmentInput extends CommitmentInput {
    private final IdentityProviderInfo ipInfo;
    private final Map<String, AnonymityRevokerInfo> arsInfos;
    private final IdentityObject idObject;
    private final JsonNode idObjectUseData;

    @Builder
    public IdentityCommitmentInput(IdentityProviderInfo ipInfo,
                                   Map<String, AnonymityRevokerInfo> arsInfos,
                                   IdentityObject idObject,
                                   BLSSecretKey idCredSec,
                                   BLSSecretKey prfKey,
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
