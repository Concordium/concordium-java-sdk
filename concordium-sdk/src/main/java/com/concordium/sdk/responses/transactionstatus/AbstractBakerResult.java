package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
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

}
