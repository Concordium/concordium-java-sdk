package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateEnqueuedPayloadResult {
    private Type updateType;
    private UpdateEnqueuedPayloadUpdateResult update;

    public enum Type {
        @JsonProperty("protocol")
        PROTOCOL
    }

}
