package com.concordium.sdk.responses;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

/**
 * Parameters that determine timeouts in the consensus protocol used from protocol version 6.
 */
@ToString
@EqualsAndHashCode
@Builder
@Getter
public class TimeoutParameters {
    /**
     * The base value for triggering a timeout
     */
    private final Duration timeoutBase;

    /**
     * Factor for increasing the timeout. Must be greater than 1.
     */
    private final Fraction timeoutIncrease;

    /**
     * Factor for decreasing the timeout. Must be between 0 and 1.
     */
    private final Fraction timeoutDecrease;

    public static TimeoutParameters from(com.concordium.grpc.v2.TimeoutParameters params) {
        return TimeoutParameters
                .builder()
                .timeoutBase(Duration.ofMillis(params.getTimeoutBase().getValue()))
                .timeoutIncrease(Fraction.from(params.getTimeoutIncrease()))
                .timeoutDecrease(Fraction.from(params.getTimeoutDecrease()))
                .build();
    }
}
