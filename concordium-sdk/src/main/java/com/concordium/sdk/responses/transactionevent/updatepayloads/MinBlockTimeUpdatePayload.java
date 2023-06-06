package com.concordium.sdk.responses.transactionevent.updatepayloads;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

/**
 * The minimum time between blocks was updated (chain parameters version 2)
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class MinBlockTimeUpdatePayload implements UpdatePayload {

    /**
     * The new minimum time between blocks.
     */
    private Duration minBlockTime;


    /**
     * Parses {@link com.concordium.grpc.v2.Duration} to {@link MinBlockTimeUpdatePayload}.
     * @param duration {@link com.concordium.grpc.v2.Duration} returned by the GRPC V2 API.
     * @return parsed {@link MinBlockTimeUpdatePayload}
     */
    public static MinBlockTimeUpdatePayload parse(com.concordium.grpc.v2.Duration duration) {
        return MinBlockTimeUpdatePayload.builder()
                .minBlockTime(Duration.ofMillis(duration.getValue()))
                .build();
    }

    @Override
    public UpdateType getType() {
        return UpdateType.MIN_BLOCK_TIME_UPDATE;
    }
}
