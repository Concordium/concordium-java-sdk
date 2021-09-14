package com.concordium.sdk.responsetypes.transactionstatus.results;

import com.concordium.sdk.responsetypes.transactionstatus.TransactionResultEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreatedResult extends TransactionResultEvent {
    private String contents;
}
