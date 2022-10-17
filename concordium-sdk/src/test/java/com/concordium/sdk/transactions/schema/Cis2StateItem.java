package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Cis2StateItem {
    private final Cis2Address address;
    private final Cis2ViewAddressState state;

    @Builder
    @JsonCreator
    public Cis2StateItem(@JsonProperty("left") Cis2Address address, @JsonProperty("right") Cis2ViewAddressState state) {
        this.address = address;
        this.state = state;
    }
}
