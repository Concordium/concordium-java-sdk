package com.concordium.sdk.responses.cryptographicparameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

//exists for json parsing only
@Getter
final class Value {

    private final byte[] bulletproofGenerators;
    private final byte[] onChainCommitmentKey;
    private final String genesisString;


    @SneakyThrows
    Value(@JsonProperty("bulletproofGenerators") String bulletproofGenerators,
          @JsonProperty("onChainCommitmentKey") String onChainCommitmentKey,
          @JsonProperty("genesisString") String genesisString) {
        this.bulletproofGenerators = Hex.decodeHex(bulletproofGenerators);
        this.onChainCommitmentKey = Hex.decodeHex(onChainCommitmentKey);
        this.genesisString = genesisString;
    }
}
