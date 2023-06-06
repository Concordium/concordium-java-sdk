package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.TimeoutParameters;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

/**
 * The consensus timeouts were updated (chain parameters version 2).
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TimeoutParametersUpdatePayload implements UpdatePayload {

    /**
     * The base value for triggering a timeout.
     */
    private Duration timeoutBase;
    /**
     * Factor for increasing the timeout.
     */
    private Fraction timeoutIncrease;
    /**
     * Factor for decreasing the timeout.
     */
    private Fraction timeoutDecrease;

    /**
     * Parses {@link TimeoutParameters} to {@link TimeoutParametersUpdatePayload}.
     * @param timeoutParameters {@link TimeoutParameters} returned by the GRPC V2 API.
     * @return parsed {@link TimeoutParametersUpdatePayload}.
     */
    public static TimeoutParametersUpdatePayload parse(TimeoutParameters timeoutParameters) {
        return TimeoutParametersUpdatePayload.builder()
                .timeoutBase(Duration.ofMillis(timeoutParameters.getTimeoutBase().getValue()))
                .timeoutIncrease(Fraction.parse(timeoutParameters.getTimeoutIncrease()))
                .timeoutDecrease(Fraction.parse(timeoutParameters.getTimeoutDecrease()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.TIMEOUT_PARAMETERS_UPDATE;
    }
}
