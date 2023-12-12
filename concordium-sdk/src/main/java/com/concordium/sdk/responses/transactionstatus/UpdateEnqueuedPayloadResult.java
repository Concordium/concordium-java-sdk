package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public final class UpdateEnqueuedPayloadResult<T> {

    private final UpdateType updateType;
    //An update can return both a 'String' and a 'UpdateEnqueuedPayloadUpdateResult'
    private final T update;

}
