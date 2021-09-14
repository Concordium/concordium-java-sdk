package com.concordium.sdk.transactions;

import lombok.*;

@Getter
@ToString
public class Account {
    private final AccountAddress address;

    private Account(AccountAddress address) {
        this.address = address;
    }

    public static Account from(String address) {
        return new Account(AccountAddress.from(address));
    }
}
