package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.UInt8;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L118">WrapParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class WrapParams extends SchemaParameter {

    private final Receiver to;
    private final UInt8[] data;


    public WrapParams(Schema schema, ReceiveName receiveName, Receiver to, UInt8[] data) {
        super(schema, receiveName);
        this.to = to;
        this.data = data;
    }
}
