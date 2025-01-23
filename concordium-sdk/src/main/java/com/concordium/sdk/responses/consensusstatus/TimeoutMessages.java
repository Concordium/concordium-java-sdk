package com.concordium.sdk.responses.consensusstatus;

import com.concordium.sdk.responses.Epoch;
import com.google.common.collect.ImmutableList;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class TimeoutMessages {

    /**
     * The first epoch for which timeout messages are present.
     */
    private final Epoch firstEpoch;

    /**
     * The timeout messages for the first epoch.
     * There should always be at least one.
     */
    @Singular
    private final ImmutableList<TimeoutMessage> firstEpochTimeouts;

    /**
     * The timeout messages for `first_epoch + 1`.
     */
    @Singular
    private final ImmutableList<TimeoutMessage> secondEpochTimeouts;
}
