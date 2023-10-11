package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1138">BalanceOfQuery</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class BalanceOfQuery<T extends TokenId> {
    @JsonProperty("token_id")
    private T tokenId;
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress address;
}
