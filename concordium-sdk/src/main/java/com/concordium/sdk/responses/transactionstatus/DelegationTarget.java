package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class DelegationTarget {

    private final DelegationType type;
    private final AccountIndex bakerId;

    DelegationTarget(@JsonProperty("delegateType") DelegationType type,
                     @JsonProperty("bakerId") AccountIndex bakerId) {
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
