package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateEnqueuedResult extends TransactionResultEvent {
    private UpdateEnqueuedPayloadResult payload;
    private long effectiveTime;
}
