package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class UpdateEnqueuedResult extends TransactionResultEvent {

    private final UpdateEnqueuedPayloadResult payload;

    @Getter
    private final long effectiveTime;

    @JsonCreator
    UpdateEnqueuedResult(@JsonProperty("payload") UpdateEnqueuedPayloadResult<Object> payload,
                         @JsonProperty("effectiveTime") long effectiveTime) {

        this.payload = payload;
        this.effectiveTime = effectiveTime;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPDATE_ENQUEUED;
    }
}
