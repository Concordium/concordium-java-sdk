package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractBakerResult extends TransactionResultEvent {
    private final UInt64 bakerId;
    private final AccountAddress account;

    @JsonCreator
    AbstractBakerResult(@JsonProperty("bakerId") long bakerId,
                        @JsonProperty("account") AccountAddress account) {
        this.bakerId = UInt64.from(bakerId);
        this.account = account;
    }
}
