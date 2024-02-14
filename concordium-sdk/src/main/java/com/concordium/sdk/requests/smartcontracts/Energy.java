package com.concordium.sdk.requests.smartcontracts;

import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents energy, i.e. the cost of executing transactions on the chain.
 */
@EqualsAndHashCode
@Builder
@Getter
public class Energy {
    private final UInt64 value;

    @Override
    public String toString() {
        return this.value.toString() + " NRG";
    }

    public static Energy from(com.concordium.grpc.v2.Energy energy) {
        return new Energy(UInt64.from(energy.getValue()));
    }

    public static Energy from(UInt64 value) {
        return new Energy(value);
    }
    public static Energy from(String val) {
        return Energy.from(UInt64.from(val));
    }

}
