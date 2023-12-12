package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import lombok.Getter;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L182">SetPausedParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class SetPausedParams extends SchemaParameter {

    private final boolean paused;
    public SetPausedParams(Schema schema, ReceiveName receiveName, boolean paused) {
        super(schema, receiveName);
        this.paused = paused;
    }
}
