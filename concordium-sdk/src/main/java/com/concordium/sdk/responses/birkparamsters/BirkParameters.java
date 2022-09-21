package com.concordium.sdk.responses.birkparamsters;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.List;
import java.util.Optional;

/**
 * The state of consensus parameters, and allowed participants (i.e., bakers).
 */
@Data
public class BirkParameters {

    /**
     * Current election difficulty.
     */
    private double electionDifficulty;

    /**
     * Leadership election nonce for the current epoch.
     */
    private Hash electionNonce;

    /**
     * The list of active bakers.
     */
    private List<Baker> bakers;

    public static Optional<BirkParameters> fromJson(ConcordiumP2PRpc.JsonResponse jsonResponse) {
        try {
            val ret = JsonMapper.INSTANCE.readValue(jsonResponse.getValue(), BirkParameters.class);

            return Optional.ofNullable(ret);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BirkParameters JSON", e);
        }
    }
}
