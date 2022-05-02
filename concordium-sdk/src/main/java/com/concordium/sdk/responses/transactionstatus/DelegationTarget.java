package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DelegationTarget {

    private final DelegationType type;
    private final String bakerId;

    DelegationTarget(@JsonProperty("delegateType") DelegationType type,
                     @JsonProperty("bakerId") String bakerId) {
        this.type = type;
        this.bakerId = bakerId;
    }

    @ToString
    public enum DelegationType {
        @JsonProperty("Passive")
        PASSIVE,
        @JsonProperty("Baker")
        BAKER
    }

}
