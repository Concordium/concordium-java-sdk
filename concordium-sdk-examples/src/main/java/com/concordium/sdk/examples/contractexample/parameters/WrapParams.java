package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L118">WrapParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class WrapParams extends SchemaParameter {

    /**
     * The address to receive these tokens.
     */
    private final Receiver to;
    /**
     * If the {@link Receiver} is a contract and the {@link Receiver} is not the invoker of the wrap function, the receive hook function is invoked with these additional data bytes as part of the input parameters.
     * Must be serialized as a json array of unsigned bytes. This serialization is implemented in {@link UInt8ByteArrrayJsonSerializer}.
     */
    @JsonSerialize(using = UInt8ByteArrrayJsonSerializer.class)
    private final byte[] data;


    public WrapParams(Schema schema, ReceiveName receiveName, Receiver to, byte[] data) {
        super(schema, receiveName);
        this.to = to;
        this.data = data;
    }
}
