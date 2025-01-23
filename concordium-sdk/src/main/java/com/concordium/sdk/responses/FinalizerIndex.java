package com.concordium.sdk.responses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The index of a particular finalizer in the finalization committee.
 */
@ToString
@EqualsAndHashCode
@Getter
public class FinalizerIndex {

    private final int value;

    public FinalizerIndex(int value) {
        this.value = value;
    }

    public static FinalizerIndex from(com.concordium.grpc.v2.FinalizerIndex finalizerIndex) {
        return new FinalizerIndex(finalizerIndex.getValue());
    }
}
