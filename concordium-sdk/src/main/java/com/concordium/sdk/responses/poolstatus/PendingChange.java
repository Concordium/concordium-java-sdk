package com.concordium.sdk.responses.poolstatus;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "pendingChangeType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PendingChangeNoChange.class, name = "NoChange"),
        @JsonSubTypes.Type(value = PendingChangeRemovePool.class, name = "RemovePool"),
        @JsonSubTypes.Type(value = PendingChangeReduceBakerCapital.class, name = "ReduceBakerCapital")
})
public abstract class PendingChange {
}
