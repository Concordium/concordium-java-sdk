package com.concordium.sdk.responses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The number of chain restarts via a protocol update. An effected
 * protocol update instruction might not change the protocol version
 * specified in the previous field, but it always increments the genesis
 * index.
 */
@ToString
@EqualsAndHashCode
@Getter
public class GenesisIndex {

    private final int value;

    public GenesisIndex(int value) {
        this.value = value;
    }

    public static GenesisIndex from(com.concordium.grpc.v2.GenesisIndex genesisIndex) {
        return new GenesisIndex(genesisIndex.getValue());
    }
}
