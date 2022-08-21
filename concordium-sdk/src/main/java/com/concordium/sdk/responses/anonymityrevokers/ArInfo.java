package com.concordium.sdk.responses.anonymityrevokers;

import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * Information on a single anonymity revoker held by the IP. Typically an IP will hold a more than one.
 */
@Data
@Getter
@ToString
public class ArInfo {

    /**
     * Unique identifier of the anonymity revoker
     */
    private UInt32 arIdentity;

    /**
     * Description of the anonymity revoker (e.g. name, contact number)
     */
    private ArDescription arDescription;

    /**
     * Elgamal encryption key of the anonymity revoker.
     */
    private ElgamalPublicKey arPublicKey;

    public static ArInfo[] fromJsonArray(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), ArInfo[].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse Anonymity Revoker JSON", e);
        }
    }
}
