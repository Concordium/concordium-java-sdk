package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public abstract class AbstractBakerResult extends TransactionResultEvent {
    private final String bakerId;
    private AccountAddress account;

    @JsonCreator
    AbstractBakerResult(@JsonProperty("bakerId") String bakerId,
                        @JsonProperty("account") String account) {
        this.bakerId = bakerId;
        if (!Objects.isNull(account)) {
            this.account = AccountAddress.from(account);
        }
    }
}
