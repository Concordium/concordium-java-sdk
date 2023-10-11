package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.UInt8;
import lombok.Getter;

import java.util.List;

@Getter
public class WrapParams extends SchemaParameter {

    private final Receiver to;
    private final List<UInt8> data;


    public WrapParams(Schema schema, ReceiveName receiveName, Receiver to, List<UInt8> data) {
        super(schema, receiveName);
        this.to = to;
        this.data = data;
    }
}
