package com.concordium.sdk.responses.transactionstatus;


import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.GTUAmount;
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
    private Hash hash;
    private AccountAddress sender;
    private GTUAmount cost;
    private final int energyCost;
    private final TransactionResult result;
    private final TransactionTypeInfo type;

    @JsonCreator
    TransactionSummary(@JsonProperty("index") int index,
                       @JsonProperty("hash") String hash,
                       @JsonProperty("sender") String sender,
                       @JsonProperty("cost") String cost,
                       @JsonProperty("energyCost") int energyCost,
                       @JsonProperty("result") TransactionResult result,
                       @JsonProperty("type") TransactionTypeInfo type) {
        this.index = index;
        if (!Objects.isNull(hash)) {
            this.hash = Hash.from(hash);
        }
        if (!Objects.isNull((sender))) {
            this.sender = AccountAddress.from(sender);
        }
        if (!Objects.isNull(cost)) {
            this.cost = GTUAmount.fromMicro(cost);
        }
        this.energyCost = energyCost;
        this.result = result;
        this.type = type;
    }
}
