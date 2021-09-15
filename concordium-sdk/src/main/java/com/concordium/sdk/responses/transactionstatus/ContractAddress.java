package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class ContractAddress extends AbstractAccount {
    private final int subIndex;
    private final int index;
    private final AccountType type;

    @JsonCreator
    ContractAddress(@JsonProperty("subindex") int subIndex,
                    @JsonProperty("index") int index,
                    @JsonProperty("type") AccountType type) {
        this.subIndex = subIndex;
        this.index = index;
        this.type = type;
    }
}
