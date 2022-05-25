package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class ContractAddress extends AbstractAddress {
    private final int subIndex;
    private final int index;
    @JsonCreator
    ContractAddress(@JsonProperty("subindex") int subIndex,
                    @JsonProperty("index") int index,
                    @JsonProperty("type") AccountType type) {
        super(type);
        this.subIndex = subIndex;
        this.index = index;
    }
}
