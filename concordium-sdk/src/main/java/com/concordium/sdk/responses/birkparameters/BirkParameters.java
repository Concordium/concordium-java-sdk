package com.concordium.sdk.responses.birkparameters;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * The state of consensus parameters, and allowed participants (i.e., bakers).
 */
@Data
@Getter
@ToString
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


    @JsonCreator
    public BirkParameters(
            @JsonProperty("electionDifficulty") double electionDifficulty,
            @JsonProperty("electionNonce") Hash electionNonce,
            @JsonProperty("bakers") List<Baker> bakers
    ) {
        this.electionDifficulty = electionDifficulty;
        this.electionNonce = electionNonce;
        this.bakers = bakers;
    }

    public static BirkParameters fromJson(ConcordiumP2PRpc.JsonResponse jsonResponse) {
        try {
            return JsonMapper.INSTANCE.readValue(jsonResponse.getValue(), BirkParameters.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse BirkParameters JSON", e);
        }
    }
}
