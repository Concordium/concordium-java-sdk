package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public final class UpdateEnqueuedResult implements TransactionResultEvent {

    private final UpdateEnqueuedPayloadResult payload;

    @Getter
    private final Timestamp effectiveTime;
    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPDATE_ENQUEUED;
    }
}
