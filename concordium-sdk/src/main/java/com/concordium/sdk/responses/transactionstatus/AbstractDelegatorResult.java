package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public abstract class AbstractDelegatorResult extends TransactionResultEvent {
    private final UInt64 delegatorId;
    private final AccountAddress delegatorAddress;

    @JsonCreator
    AbstractDelegatorResult(@JsonProperty("delegatorId") long delegatorId,
                            @JsonProperty("account") AccountAddress delegatorAddress) {
        this.delegatorId = UInt64.from(delegatorId);
        this.delegatorAddress = delegatorAddress;
    }


}
