package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.types.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A query for the balance of a given address for a given token.
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1138">BalanceOfQuery</a>' used in different smart contracts
 */
@Getter
@AllArgsConstructor
public class BalanceOfQuery<T extends TokenId> {
    /**
     * The ID of the token for which to query the balance of.
     * Field names must match field names in corresponding rust struct, therefore the annotation.
     */
    @JsonProperty("token_id")
    private T tokenId;
    /**
     * The address for which to query the balance of. An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private AbstractAddress address;
}
