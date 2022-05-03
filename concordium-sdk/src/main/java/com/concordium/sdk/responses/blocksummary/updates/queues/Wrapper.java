package com.concordium.sdk.responses.blocksummary.updates.queues;

import com.concordium.sdk.types.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Wrapper<T> {

    private final Timestamp effectiveTime;
    private final T update;

    Wrapper(long effectiveTimeUnixTimestamp, T update) {
        this.effectiveTime = Timestamp.newSeconds(effectiveTimeUnixTimestamp);
        this.update = update;
    }
}
