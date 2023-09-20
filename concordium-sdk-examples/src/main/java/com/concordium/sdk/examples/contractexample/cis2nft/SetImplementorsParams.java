package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.ContractAddress;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter 'SetImplementorsParams' in the <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft example</a>
 * Used with contract function 'setImplementors'
 * Takes a standard identifier and list of contract addresses providing implementations of this standard.
 */
@Getter
public class SetImplementorsParams extends SchemaParameter {

    /**
     * The identifier for the standard
     */
    private final String id;
    /**
     * The addresses of the implementors of the standard.
     */
    private final List<ContractAddress> implementors;

    public SetImplementorsParams(Schema schema, ReceiveName receiveName, String id, List<ContractAddress> implementors) {
        super(schema, receiveName);
        this.id = id;
        this.implementors = implementors;
    }
}
