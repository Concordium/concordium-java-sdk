package com.concordium.sdk.responsetypes.transactionstatus.results.baker;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractBakerResult extends TransactionResultEvent {
    private String bakerId;
    private String account;
}
