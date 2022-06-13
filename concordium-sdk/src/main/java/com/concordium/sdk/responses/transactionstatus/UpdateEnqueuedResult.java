package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.Timestamp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class UpdateEnqueuedResult extends TransactionResultEvent {

    private final UpdateEnqueuedPayloadResult payload;

    @Getter
    private final Timestamp effectiveTime;

    @JsonCreator
    UpdateEnqueuedResult(@JsonProperty("payload") UpdateEnqueuedPayloadResult<Object> payload,
                         @JsonProperty("effectiveTime") long effectiveTime) {
        this.payload = payload;
        this.effectiveTime = Timestamp.newSeconds(effectiveTime);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPDATE_ENQUEUED;
    }
}
