package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DelegationTarget {

    private final DelegationType type;
    private final UInt64 bakerId;

    DelegationTarget(@JsonProperty("delegateType") DelegationType type,
                     @JsonProperty("bakerId") long bakerId) {
        this.type = type;
        this.bakerId = UInt64.from(bakerId);
    }

    @ToString
    public enum DelegationType {
        @JsonProperty("Passive")
        PASSIVE,
        @JsonProperty("Baker")
        BAKER
    }

}
