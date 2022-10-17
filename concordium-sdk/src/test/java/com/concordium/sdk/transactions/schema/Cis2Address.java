package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.ContractAddress;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Jacksonized
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class Cis2Address {
    @JsonProperty("Account")
    private final List<AccountAddress> account;

    @JsonProperty("Contract")
    private final List<ContractAddress> contract;

    Cis2Address(AccountAddress account, ContractAddress contract) {
        this.account = Objects.isNull(account) ? null : Arrays.asList(account);
        this.contract = Objects.isNull(contract) ? null : Arrays.asList(contract);
    }

    Cis2Address(AccountAddress account) {
        this(account, null);
    }

    Cis2Address(ContractAddress contract) {
        this(null, contract);
    }

    public static Cis2Address from(AccountAddress address) {
        return new Cis2Address(address);
    }

    public static Cis2Address from(ContractAddress contractAddress) {
        return new Cis2Address(contractAddress);
    }
}
