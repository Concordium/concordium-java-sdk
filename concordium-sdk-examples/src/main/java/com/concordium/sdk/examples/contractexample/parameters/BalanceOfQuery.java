package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceOfQuery<T extends TokenId> {
    @JsonProperty("token_id")
    private T tokenId;
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress address;
}
