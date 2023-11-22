package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A query for the operator of a given address for a given token.
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1175">OperatorOfQuery</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class OperatorOfQuery {

    /**
     * An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress owner;
    /**
     * The address for which to check for being an operator of the owner. An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress address;
}
