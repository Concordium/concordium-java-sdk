package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public abstract class AbstractDelegatorResult extends TransactionResultEvent {
    private final AccountIndex delegatorId;
    private final AccountAddress delegatorAddress;

    @JsonCreator
    AbstractDelegatorResult(@JsonProperty("delegatorId") AccountIndex delegatorId,
                            @JsonProperty("account") AccountAddress delegatorAddress) {
        this.delegatorId = delegatorId;
        this.delegatorAddress = delegatorAddress;
    }


}
