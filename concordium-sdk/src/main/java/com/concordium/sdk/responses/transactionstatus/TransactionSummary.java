package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class TransactionSummary {
    private final int index;
    private final Hash hash;
    private final AccountAddress sender;
    private final CCDAmount cost;
    private final int energyCost;
    private final TransactionResult result;
    private final TransactionTypeInfo type;

    @JsonCreator
    TransactionSummary(@JsonProperty("index") int index,
                       @JsonProperty("hash") Hash hash,
                       @JsonProperty("sender") AccountAddress sender,
                       @JsonProperty("cost") CCDAmount cost,
                       @JsonProperty("energyCost") int energyCost,
                       @JsonProperty("result") TransactionResult result,
                       @JsonProperty("type") TransactionTypeInfo type) {
        this.index = index;
        this.hash = hash;
        this.sender = sender;
        this.cost = cost;
        this.energyCost = energyCost;
        this.result = result;
        this.type = type;
    }
}
