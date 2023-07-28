package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode
public abstract class AbstractBakerResult implements TransactionResultEvent {
    private final BakerId bakerId;
    private final AccountAddress account;

    @JsonCreator
    AbstractBakerResult(@JsonProperty("bakerId") AccountIndex bakerId,
                        @JsonProperty("account") AccountAddress account) {
        this.bakerId = BakerId.from(bakerId);
        this.account = account;
    }

}
