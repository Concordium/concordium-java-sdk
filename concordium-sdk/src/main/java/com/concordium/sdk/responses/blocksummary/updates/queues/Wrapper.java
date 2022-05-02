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


    Wrapper(Timestamp effectiveTime, T update) {
        this.effectiveTime = effectiveTime;
        this.update = update;
    }
}
