package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


/**
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1073">Transfer</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class Transfer<T extends TokenId, A extends TokenAmount> {
    @JsonProperty("token_id")
    private T tokenId;
    private A amount;
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress from;
    private Receiver to;
    private List<UInt8> data;


}
