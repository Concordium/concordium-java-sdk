package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
