package com.concordium.sdk.responses.cryptographicparameters;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class CryptographicParameters {

    /**
     * The version of the cryptographic parameters.
     */
    private final int version;

    /**
     * The bullet proof generators
     */
    private final byte[] bulletproofGenerators;

    /**
     * The on chain commitment key
     */
    private final byte[] onChainCommitmentKey;

    /**
     * The genesis string.
     */
    private final String genesisString;

    @JsonCreator
    public CryptographicParameters(
            @JsonProperty("v") int version,
            @JsonProperty("value") Value value) {
        this.version = version;
        this.bulletproofGenerators = value.getBulletproofGenerators();
        this.onChainCommitmentKey = value.getOnChainCommitmentKey();
        this.genesisString = value.getGenesisString();
    }

    public static CryptographicParameters from(String json) {
        try {
            return JsonMapper.INSTANCE.readValue(json, CryptographicParameters.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse ConsensusStatus JSON", e);
        }
    }
}
