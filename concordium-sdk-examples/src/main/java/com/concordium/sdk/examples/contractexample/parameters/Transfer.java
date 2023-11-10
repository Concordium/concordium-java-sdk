package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


/**
 * A single transfer of some amount of a token.
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1073">Transfer</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class Transfer<T extends TokenId, A extends TokenAmount> {
    /**
     * The ID of the token being transferred.
     * Field names must match field names in corresponding rust struct, therefore the annotation.
     */
    @JsonProperty("token_id")
    private T tokenId;
    /**
     * The amount of tokens being transferred.
     */
    private A amount;
    /**
     * The address owning the tokens being transferred. An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress from;
    /**
     * The address receiving the tokens being transferred. A {@link Receiver} is either an {@link AccountAddress} or a {@link ContractAddress} alongside a function to call on the receiving contract.
     */
    private Receiver to;
    /**
     * Additional data to include in the transfer. Can be used for additional arguments
     */
    private List<UInt8> data;


}
