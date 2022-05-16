package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class Account extends AbstractAccount {
    private final AccountAddress address;

    @JsonCreator
    Account(@JsonProperty("type") AccountType type,
            @JsonProperty("address") String address) {
        super(type);
        this.address = AccountAddress.from(address);

    }
}
