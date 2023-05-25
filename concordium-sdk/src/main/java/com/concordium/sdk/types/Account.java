package com.concordium.sdk.types;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Account extends AbstractAddress {
    private final AccountAddress address;

    @JsonCreator
    Account(@JsonProperty("type") AccountType type,
            @JsonProperty("address") String address) {
        super(type);
        this.address = AccountAddress.from(address);

    }

    Account(@JsonProperty("type") AccountType type,
            @JsonProperty("address") AccountAddress address) {
        super(type);
        this.address = address;

    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link Account}.
     * @param account {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link Account}.
     */
    public static Account parse(com.concordium.grpc.v2.AccountAddress account) {
        return new Account(AccountType.ADDRESS_ACCOUNT ,AccountAddress.parse(account));
    }
}
