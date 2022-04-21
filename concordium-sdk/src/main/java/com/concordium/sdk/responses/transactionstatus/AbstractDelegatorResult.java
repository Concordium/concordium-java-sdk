package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public abstract class AbstractDelegatorResult extends TransactionResultEvent {
    private final String delegatorId;
    private AccountAddress delegatorAddress;

    @JsonCreator
    AbstractDelegatorResult(@JsonProperty("delegatorId") String delegatorId,
                            @JsonProperty("account") String delegatorAddress) {
        this.delegatorId = delegatorId;
        if (!Objects.isNull(delegatorAddress)) {
            this.delegatorAddress = AccountAddress.from(delegatorAddress);
        }
    }
}
