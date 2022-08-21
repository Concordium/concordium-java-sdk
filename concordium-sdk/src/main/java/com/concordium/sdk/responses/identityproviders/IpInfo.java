package com.concordium.sdk.responses.identityproviders;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IpInfo {

    /**
     * Unique identifier of the identity provider.
     */
    public UInt32 ipIdentity;

    /**
     * Free form description, e.g., how to contact them off-chain
     */
    public IpDescription ipDescription;

    /**
     * PS public key of the IP.
     */
    public PSPublicKey ipVerifyKey;

    /**
     * Ed public key of the IP
     */
    private ED25519PublicKey ipCdiVerifyKey;

    public static IpInfo[] fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), IpInfo[].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse AccountInfo JSON", e);
        }
    }
}
