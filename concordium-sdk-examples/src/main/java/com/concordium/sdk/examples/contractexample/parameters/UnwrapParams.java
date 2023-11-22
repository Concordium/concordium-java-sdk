package com.concordium.sdk.examples.contractexample.parameters;


import com.concordium.sdk.transactions.AccountType;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L102">UnwrapParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class UnwrapParams extends SchemaParameter {
    /**
     * The amount of tokens to unwrap.
     */
    private final String amount;
    /**
     * The owner of the tokens. An {@link AbstractAddress} is either an {@link AccountAddress} or a {@link ContractAddress}.
     * Fields of smart contract parameters containing {@link AbstractAddress} must be annotated with '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     * to ensure correct serialization.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private final AbstractAddress owner;
    /**
     * The address to receive these unwrapped CCD. A {@link Receiver} is either an {@link AccountAddress} or a {@link ContractAddress} alongside a function to call on the receiving contract.
     */
    private final Receiver receiver;
    /**
     * If the {@link Receiver} is a {@link AccountType#ADDRESS_CONTRACT} the unwrapped CCD together with these additional data bytes are sent to the function entrypoint specified in the {@link Receiver}.
     */
    private final UInt8[] data;



    public UnwrapParams(Schema schema, ReceiveName receiveName, String amount, AbstractAddress owner, Receiver receiver, UInt8[] data) {
        super(schema, receiveName);
        this.amount = amount;
        this.owner = owner;
        this.receiver = receiver;
        this.data = data;
    }
}
