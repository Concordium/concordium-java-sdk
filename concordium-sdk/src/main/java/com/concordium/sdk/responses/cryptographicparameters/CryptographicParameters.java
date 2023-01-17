package com.concordium.sdk.responses.cryptographicparameters;

import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Data;

/**
 * A Set of Cryptographic parameters that are particular to the chain and
 * shared by everybody who interacts with the chain.
 */
@Data
@Builder
public final class CryptographicParameters {

    /**
     * The version of the cryptographic parameters.
     */
    private final int version;

    /**
     * The bulletproof generators
     */
    private final BulletproofGenerators bulletproofGenerators;

    /**
     * The on chain commitment key
     */
    private final PedersenCommitmentKey onChainCommitmentKey;

    /**
     * The genesis string.
     */
    private final String genesisString;

    @JsonCreator
    public static CryptographicParameters fromJsonTypes(
            @JsonProperty("v") int version,
            @JsonProperty("value") Value value) {
        return new CryptographicParameters(
                version,
                value.getBulletproofGenerators(),
                value.getOnChainCommitmentKey(),
                value.getGenesisString()
        );
    }

    public static CryptographicParameters from(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, CryptographicParameters.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse CryptographicParameters JSON", e);
        }
    }
}
