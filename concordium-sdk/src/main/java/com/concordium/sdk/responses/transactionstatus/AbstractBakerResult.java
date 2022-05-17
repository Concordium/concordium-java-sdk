package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractBakerResult extends TransactionResultEvent {
    private final AccountIndex bakerId;
    private final AccountAddress account;

    @JsonCreator
    AbstractBakerResult(@JsonProperty("bakerId") AccountIndex bakerId,
                        @JsonProperty("account") AccountAddress account) {
        this.bakerId = bakerId;
        this.account = account;
    }
}
