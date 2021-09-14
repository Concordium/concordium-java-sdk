package com.concordium.sdk.responsetypes.transactionstatus;

import com.concordium.sdk.transactions.AccountType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {
    private AccountType type;
    private String address;
}
