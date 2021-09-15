package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public final class UpdateEnqueuedPayloadResult<T> {

    private final UpdateType updateType;
    //An update can return both a 'String' and a 'UpdateEnqueuedPayloadUpdateResult'
    private final T update;

    @JsonCreator
    UpdateEnqueuedPayloadResult(@JsonProperty("updateType") UpdateType updateType,
                                @JsonProperty("update") T update) {
        this.updateType = updateType;
        this.update = update;
    }

}
